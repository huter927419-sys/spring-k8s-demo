/**
 * main.js - Vue Application Entry Point
 * Vue 应用程序入口文件
 * 
 * @description
 * This is the main entry point of the Vue.js application.
 * It initializes the Vue app, registers plugins (Element Plus, Router),
 * and mounts the application to the DOM.
 * 
 * 这是 Vue.js 应用程序的主入口点。
 * 它初始化 Vue 应用，注册插件（Element Plus、Router），
 * 并将应用程序挂载到 DOM。
 */

import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'

// Create Vue application instance
// 创建 Vue 应用程序实例
const app = createApp(App)

// Register all Element Plus icons globally
// 全局注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// Register plugins / 注册插件
app.use(ElementPlus)  // Element Plus UI library / Element Plus UI 库
app.use(router)       // Vue Router for navigation / Vue Router 用于导航

// Mount application to DOM element with id 'app'
// 将应用程序挂载到 id 为 'app' 的 DOM 元素
app.mount('#app')

