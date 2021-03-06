var recipeApi = Vue.resource('/recipe');

Vue.component('recipe-form', {
    props: ['recipes'],
    data: function () {
        return {
            recipeId: 'null',
            name: '',
            description: '',
            created: 'null',
            parentRecipe: 'null'
        }
    },
    template: '<div>' +
        '<input type="text" placeholder="Whrite name" v-model="name">' +
        '<input type="text" placeholder="Whrite description" v-model="description">' +
        '<input type="button" value="Save" @click="save">' +
        '</div>',
    methods: {
        save: function () {
            var recipeDto = [
                {recipeId: this.recipeId},
                {name: this.name},
                {description: this.description},
                {created: this.created},
                {parentRecipe: this.parentRecipe}
            ];

            recipeApi.save({}, {recipeDto}).then(result =>
                result.json().then(data => {
                    this.recipes.push(data);
                    this.name = '';
                    this.description = '';
                })
            )
        }
    }
});

Vue.component('recipe', {
    props: ['recipe'],
    template: '<div><i>({{recipe.recipeId}})</i>{{recipe.name}} {{recipe.description}}</div>'
})

Vue.component('recipe-list', {
    props: ['recipes'],
    template: '<div>' +
        '<recipe-form :recipes="recipes"/>' +
        '<recipe v-for="recipe in recipes" :key="recipe.recipeId"  :recipe="recipe"/>' +
        '</div>',
    created: function () {
        recipeApi.get().then(result =>
            result.json().then(data =>
                data.forEach(recipe => this.recipes.push(recipe))
            )
        )
    }
});

var app = new Vue({
    el: '#app',
    template: '<recipe-list :recipes="recipes"/>',
    data: {
        recipes: []
    }
});