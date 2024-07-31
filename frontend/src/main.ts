import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/utils/axios-interceptors';
import '@/index.css'
import '@/assets/styles.scss';
import 'primeicons/primeicons.css'
import ToastService from 'primevue/toastservice';
import store from '@/store/index'
import { FontAwesomeIcon } from '@/plugins/fontawesome'
import PrimeVue, { defaultOptions } from 'primevue/config';
import Aura from '@primevue/themes/aura';


createApp(App)
    .use(router)
    .use(store)
    .use(ToastService)
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
    .mount('#app')
