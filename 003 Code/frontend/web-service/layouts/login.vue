<template>
  <v-app class="login-page">
    <h1 style="padding: 20px">WCD CAPSTONE</h1>
    <div class="centered-content" style="height:100%;">
      <Nuxt />
    </div>
  </v-app>
</template>

<style scoped>
.login-page {
  background: rgba(0, 0, 0, 0.4); /* 배경 색상과 투명도 조절 */
  background-image: url("https://cdn.pixabay.com/photo/2023/06/14/02/21/stairs-8062145_640.jpg"); 
  background-blend-mode: overlay; /* 필요한 경우 배경 이미지와 조합 */
}
.centered-content {
  display: flex;
  justify-content: center; /* 수평 가운데 정렬 */
  align-items: center;
}
</style>