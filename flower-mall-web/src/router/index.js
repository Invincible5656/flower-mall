import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'Login',
            component: () => import('../views/Login.vue')
        },

        {
            path: '/',
            name: 'Home',
            component: () => import('../views/Home.vue'),
            meta: { requiresAuth: true }
        },

        //购物车
        {
            path: '/cart',
            name: 'Cart',
            component: () => import('../views/Cart.vue'),
            meta: { requiresAuth: true } // 必须登录
        },

        //注册
        {
            path: '/register',
            name: 'Register',
            component: () => import('../views/Register.vue')
        },

        {
            path: '/my-orders',
            name: 'MyOrders',
            component: () => import('../views/MyOrders.vue'),
            meta: { requiresAuth: true }
        },

        {
            path: '/admin',
            name: 'Admin',
            // 这里建议用一个 Layout 组件包裹，现在为了简单直接跳到商品列表
            redirect: '/admin/products',
            children: [
                {
                    path: 'products',
                    name: 'ProductManage',
                    component: () => import('../views/admin/ProductManage.vue'),
                    meta: {
                        requiresAuth: true,
                        role: 'admin'  // 【重要】标记只有 admin 能进
                    }
                },

                {
                    path: 'species',
                    name: 'SpeciesManage',
                    component: () => import('../views/admin/SpeciesManage.vue'),
                    meta: { requiresAuth: true, role: 'admin' }
                },

                {
                    path: 'orders', // 访问路径: /admin/orders
                    name: 'OrderManage',
                    component: () => import('../views/admin/OrderManage.vue'),
                    meta: { requiresAuth: true, role: 'admin' }
                }
            ]
        }
    ]
})

// 【重点2】路由守卫（保安）
// 每次页面跳转前，都会执行这个函数
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('ACCESS_TOKEN')
    const userRole = localStorage.getItem('USER_ROLE')

    // 1. 判断该页面是否需要登录
    if (to.meta.requiresAuth) {
        // 1.1 如果没 Token -> 滚去登录
        if (!token) {
            next('/login')
            return
        }

        // 1.2 【新增逻辑】如果页面指定了角色 (比如 'admin')
        if (to.meta.role) {
            // 拿用户的角色跟页面要求的角色比对
            if (userRole !== to.meta.role) {
                // 角色不对 (比如 user 想进 admin 页面)
                alert('无权访问非本人角色的页面！') // 弹窗警告
                next('/') // 踢回首页
                return
            }
        }

        // 验证通过，放行
        next()
    } else {
        // 不需要登录的页面，直接放行
        next()
    }
})

export default router