<template>
  <v-card style="width: 50%">
    <v-card-title
      >{{ title }}<v-spacer />
      <v-btn to="/club-manage">더보기</v-btn></v-card-title
    >
    <v-list>
      <v-list-item
        v-for="(myclub, i) in displayedClubs"
        :key="i"
        :to="`/clubs/${myclub.id}`"
        router
        exact
      >
        <v-list-item-action>
          <img
            :src="myclub.mainImageUrl"
            style="height: 45px; width: 45px; border-radius: 5px;"
          />
        </v-list-item-action>
        <v-list-item-content>
          <v-list-item-title>{{ myclub.clubName }}</v-list-item-title>
          <p style="margin: 0">{{ myclub.description }}</p>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-card>
</template>
  
  <script>
export default {
  name: "MyClubCard",
  data() {
    return {
      myclubs: [],
      title: "내가 만든 모임",
    };
  },
  methods: {
    async getMyClubs() {
      try {
        const access_token = this.$store.state.access_token;
        const user_id = sessionStorage.getItem("user_id");
        const config = {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${access_token}`,
          },
        };
        await this.$axios
          .get(`/club-service/users/${user_id}/clubs`, config)
          .then((res) => {
            console.log(res.data);
            this.myclubs = res.data;
          });
      } catch (err) {
        console.log(err);
      }
    },
    getImageDataUri(imageData) {
      return `data:image/jpg;base64,${imageData}`;
    },
  },
  computed: {
    displayedClubs() {
      // 최대 5개 클럽만 표시
      const user_id = sessionStorage.getItem("user_id");
      return this.myclubs
        .filter((myclub) => myclub.hostId == user_id)
        .slice(0, 5);
    },
  },
  created() {
    if (process.client) {
      this.getMyClubs();
    }
  },
};
</script>
  