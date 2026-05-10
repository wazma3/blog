import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
  },
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: () => import('../views/ArticleDetail.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/Layout.vue'),
    redirect: '/admin/articles',
    children: [
      {
        path: 'articles',
        name: 'AdminArticles',
        component: () => import('../views/admin/Articles.vue'),
      },
      {
        path: 'articles/new',
        name: 'AdminArticleNew',
        component: () => import('../views/admin/ArticleEditor.vue'),
      },
      {
        path: 'articles/edit/:id',
        name: 'AdminArticleEdit',
        component: () => import('../views/admin/ArticleEditor.vue'),
      },
      {
        path: 'tags',
        name: 'AdminTags',
        component: () => import('../views/admin/Tags.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path.startsWith('/admin') && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
