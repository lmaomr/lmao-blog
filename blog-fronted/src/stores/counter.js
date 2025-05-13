// 1. 从 Vue 中导入所需的响应式 API
import { ref, computed } from 'vue'
// 2. 从 Pinia 中导入定义 Store 的方法
import { defineStore } from 'pinia'

// 3. 使用 `defineStore` 定义一个名为 'counter' 的 Store
//    第二个参数是组合式(Composition API)写法函数
export const useCounterStore = defineStore('counter', () => {
  // 4. 定义响应式数据 count，初始值为 0
  //    ref() 会创建一个响应式引用，通过 .value 访问
  const count = ref(0)

  // 5. 定义计算属性 doubleCount
  //    当 count 变化时，doubleCount 会自动重新计算
  const doubleCount = computed(() => count.value * 2)

  // 6. 定义方法 increment，用于修改状态
  function increment() {
    count.value++ // 注意需要通过 .value 修改 ref 的值
  }

  // 7. 返回这些属性和方法
  //    在组件中使用时会自动解构，无需 .value
  return { count, doubleCount, increment }
})