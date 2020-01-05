new Vue({
    el: '#main',
    data: {
        state: 'SignIn'
    },
    methods:{
        aClick(){
            this.state = this.state === 'SignUp' ? 'SignIn' : 'SignUp';
        }
    }
})