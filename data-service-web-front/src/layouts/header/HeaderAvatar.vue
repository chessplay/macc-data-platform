<!--
 * @Author: your name
 * @Date: 2021-10-15 17:16:58
 * @LastEditTime: 2021-11-25 11:10:43
 * @LastEditors: your name
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \baichuan-web\src\layouts\header\HeaderAvatar.vue
-->
<template>
  <a-dropdown>
    <div class="header-avatar" style="cursor: pointer">
      <a-avatar class="avatar" size="small" shape="circle" icon="user" />
      <span class="name">{{ user.userName }}</span>
      <!-- <span class="name">管理员</span> -->
    </div>
    <a-menu :class="['avatar-menu']" slot="overlay">
      <!-- <a-menu-item>
        <a-icon type="user" />
        <span>个人中心</span>
      </a-menu-item>
      <a-menu-item>
        <a-icon type="setting" />
        <span>设置</span>
      </a-menu-item>
      <a-menu-divider />-->
      <a-menu-item @click="logout">
        <a-icon style="margin-right: 8px;" type="poweroff" />
        <span>退出登录</span>
      </a-menu-item>
    </a-menu>
  </a-dropdown>
</template>

<script>
import { mapGetters } from "vuex";
import { logout } from "@/services/user";

export default {
  name: "HeaderAvatar",
  computed: {
    ...mapGetters("account", ["user"]),
  },
  methods: {
    logout() {
      logout().then((ret) => {
        const backUrl = window.location.origin + window.location.pathname;
        if (ret.code == 200) {
          //清除TABS缓存
          sessionStorage.removeItem(process.env.VUE_APP_TBAS_KEY);
          window.location = `${window.location.origin}${process.env.VUE_APP_API_BASE_URL}/sso/login?back=${backUrl}`;
        }
      });
    },
  },
};
</script>

<style lang="less">
.header-avatar {
  display: inline-flex;
  .avatar,
  .name {
    align-self: center;
  }
  .avatar {
    margin-right: 8px;
    background-color: #2b6afd;
  }
  .name {
    font-weight: 500;
  }
}
.avatar-menu {
  width: 150px;
}
</style>
