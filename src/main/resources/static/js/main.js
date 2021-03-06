function getIndex(list, id) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        } else {
            return -1;
        }
    }
}

var recipeApi = Vue.resource('/recipe{/id}');

Vue.component('recipe-form', {
    props: ['recipes', 'editAttr'],
    data: function () {
        return {
            id: '',
            name: '',
            description: '',
            parentId: ''
        }
    },
    watch: {
        editAttr: function (newVal, oldVal) {
            this.name = newVal.name;
            this.description = newVal.description;
            this.parentId = newVal.parentId;
            this.id = newVal.id;
        }
    },
    template: '<div>' +
        '<input type="text" placeholder="Write name" v-model="name">' +
        '<input type="text" placeholder="Write description" v-model="description">' +
        '<input type="text" placeholder="Write parent recipe id" v-model="parentId">' +
        '<input type="button" value="Save" @click="save">' +
        '</div>',
    methods: {
        save: function () {
            var recipeDto =
                {
                    id: this.id,
                    name: this.name,
                    description: this.description,
                };
            if (this.id) {
                recipeApi.$http.put({}, recipeDto)
                    .then(result => result.json()
                        .then(data => {
                            var index = getIndex(this.recipes, data.id);
                            this.recipes.splice(index, 1, data);
                        }));
            } else {
                recipeApi.save(this.parentId, recipeDto).then(result =>
                    result.json().then(data => {
                        this.recipes.push(data);
                        this.name = '';
                        this.description = '';
                        this.parentId = '';
                    })
                )
            }
        }
    }
});

Vue.component('recipe', {
    props: ['recipe', 'editRecipe'],
    template: '<div><i>({{recipe.recipeId}})</i>{{recipe.name}} {{recipe.description}}' +
        '<span>' +
        '<input type="button" value="Edit" @click="edit">' +
        '</span>' +
        '</div>',
    methods: {
        edit: function () {
            this.editRecipe(this.recipe);
        }
    }
})

Vue.component('recipe-list', {
    props: ['recipes'],
    data: function () {
        return {
            recipe: null
        }
    },
    methods: {
        editRecipe: function (recipe) {
            this.recipe = recipe;
        }
    },
    template: '<div>' +
        '<recipe-form :recipes="recipes" :editAttr="recipe"/>' +
        '<recipe v-for="recipe in recipes" :key="recipe.recipeId"  :recipe="recipe" :editRecipe="editRecipe"/>' +
        '</div>',
    created: function () {
        recipeApi.get().then(result =>
            result.json().then(data =>
                data.forEach(recipe => this.recipes.push(recipe))
            )
        )
    }
});

var recipeList = new Vue({
    el: "#app",
    template: '<recipe-list :recipes="recipes"/>',
    data: {
        recipes: []
    }
})