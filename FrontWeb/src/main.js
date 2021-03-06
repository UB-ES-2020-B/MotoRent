// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import '@/../bootstrap/css/bootstrap.css'
import BootstrapVue from 'bootstrap-vue'
import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'
import router from './router'
import firebase from 'firebase/app'
import axios from 'axios'
import createPersistedState from 'vuex-persistedstate'
import * as VueGoogleMaps from 'vue2-google-maps'

Vue.prototype.$axios = axios
Vue.use(BootstrapVue)
Vue.config.productionTip = false
Vue.prototype.$heroku = 'https://motorent-deploy.herokuapp.com'
Vue.use(VueGoogleMaps, {
  load: {
    key: 'AIzaSyDNBDKhSoYVhvd8SWPVCavLPspiE1UJ01c',
    libraries: 'places' // necessary for places input
  }
})
Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    token: ''
  },
  mutations: {
    setToken (state, newToken) {
      state.token = newToken
    }
  },
  getters: {
    isLoggedIn: state => {
      return state.token !== ''
    }
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})

const configOptions = {
  apiKey: 'AIzaSyAsADSDi32pUw1L_GMHmuQF5bIHODKALIQ',
  authDomain: 'motorent-7cb40.firebaseapp.com',
  databaseURL: 'https://motorent-7cb40.firebaseio.com',
  projectId: 'motorent-7cb40',
  storageBucket: 'motorent-7cb40.appspot.com',
  messagingSenderId: '657403564853',
  appId: '1:657403564853:web:4c7ed58267a6fc1939b27c'
}

firebase.initializeApp(configOptions)
Vue.use(require('vue-moment'))

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App),
  components: { App },
  template: '<App/>',
  store: store
}).$mount('#app')
