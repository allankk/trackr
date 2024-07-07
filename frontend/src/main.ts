import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './index.css'
import 'primeicons/primeicons.css'
import store from './store/index'
import { FontAwesomeIcon } from './plugins/fontawesome'
import PrimeVue from 'primevue/config';
import Aura from '@primevue/themes/aura';


createApp(App)
    .use(router)
    .use(store)
    .use(PrimeVue, {
        theme: {
            preset: Aura
        }
    })
    .component("font-awesome-icon", FontAwesomeIcon)
    .mount('#app')
