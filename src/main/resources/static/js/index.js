new Vue({
    el: '#main',
    data: {
        state: 'SignIn',
        actor: 'author/Login'
    },
    methods:{
        aClick(){
            this.state = this.state === 'SignUp' ? 'SignIn' : 'SignUp';
            this.actor = this.actor === 'author/Login' ? 'author/register' : 'author/login';
        }
    }
})