<template>
  <exception-page home-route="/dashboard/workplace" class="dc-page-card" type="500" />
</template>

<script>
import ExceptionPage from '@/components/exception/ExceptionPage'
import {mapState} from 'vuex'
export default {
  name: 'Exp500',
  components: {ExceptionPage},
  computed: {
    ...mapState('setting', ['pageMinHeight']),
    minHeight() {
      return this.pageMinHeight ? this.pageMinHeight + 'px' : '100vh'
    }
  }
}
</script>

<style scoped lang="less">
</style>
