import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './index.css'
import store from './store/index'
import { FontAwesomeIcon } from './plugins/fontawesome'


createApp(App)
    .use(router)
    .use(store)
    .component("font-awesome-icon", FontAwesomeIcon)
    .mount('#app')
