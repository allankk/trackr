import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './index.css'
import '@/assets/styles.scss';
import 'primeicons/primeicons.css'
import store from './store/index'
import { FontAwesomeIcon } from './plugins/fontawesome'
import PrimeVue, { defaultOptions } from 'primevue/config';
import Aura from '@primevue/themes/aura';
import AnimateOnScroll from 'primevue/animateonscroll';

createApp(App)
    .use(router)
    .use(store)
    .use(PrimeVue, {
        theme: {
            preset: Aura
        },
        locale: {
            ...defaultOptions.locale,
            firstDayOfWeek: 1,
        },
    })
    .component("font-awesome-icon", FontAwesomeIcon)
    .directive('animateonscroll', AnimateOnScroll)
    .mount('#app')
