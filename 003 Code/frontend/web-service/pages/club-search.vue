<template>
  <div style="width: 100%; padding: 30px; text-align: center">
    <div class="input-container" style="margin-bottom: 30px">
      <v-icon class="icon">mdi-magnify</v-icon>
      <input
        type="text"
        id="search"
        name="search"
        placeholder="검색어를 입력하세요..."
        v-model="searchKeyword"
      />
    </div>
    <v-select
      v-model="selectedFilter"
      :items="filterOptions"
      label="검색 필터"
    ></v-select>

    <v-card color="none">
      <v-row>
        <v-col
          v-for="(club, i) in filteredClubs"
          :key="i"
          cols="6"
          style="padding: 0"
        >
          <v-list-item :to="`/clubs/${club.id}`" router exact>
            <v-list-item-content>
              <div class="club-item">
                <img
                  :src="getImageDataUri(club.multipartFile)"
                  class="club-logo"
                />
                <v-list-item-title class="club-name">{{
                  club.clubName
                }}</v-list-item-title>
                <v-list-item-subtitle class="club-description">{{
                  club.description
                }}</v-list-item-subtitle>
              </div>
            </v-list-item-content>
          </v-list-item>
        </v-col>
      </v-row>
    </v-card>
    <!-- <v-pagination
          v-model="pageNumber"
          :length="totalPages"
          :total-visible="10"
          @input="handlePageChange"
        ></v-pagination> -->
  </div>
</template>


    
  <script>
export default {
  data() {
    return {
      clubs: [],
      searchKeyword: "", // 검색어
      filteredClubs: [],
      selectedFilter: "모임 이름",
      filterOptions: ["모임 이름", "모임 설명", "카테고리"],
      // pageNumber:1,
      // totalPages:null,
    };
  },
  methods: {
    async getClubs() {
      try {
        const access_token = this.$store.state.access_token;
        const config = {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${access_token}`,
          },
          params: {
            page:0,
            size:30,
          }
        };
        const response = await this.$axios.get(`/club-service/clubs`, config);
        console.log(response.data.content);
        this.clubs = response.data.content;
        this.filteredClubs = response.data.content;
      } catch (err) {
        console.error("Error while fetching clubs:", err);
        this.clubs = []; // 에러 발생 시 빈 배열로 초기화
      }
    },
    getImageDataUri(imageData) {
      return `data:image/jpg;base64,${imageData}`;
    },
    updateFilteredClubs() {
      if (!this.searchKeyword) {
        // 검색어가 비어 있을 때, 초기 데이터를 그대로 사용
        this.filteredClubs = this.clubs;
      } else {
        if (this.selectedFilter === "모임 이름") {
          this.filteredClubs = this.clubs.filter((club) =>
            club.clubName.includes(this.searchKeyword)
          );
        } else if (this.selectedFilter === "모임 설명") {
          this.filteredClubs = this.clubs.filter((club) =>
            club.description.includes(this.searchKeyword)
          );
        } else if (this.selectedFilter === "카테고리") {
          this.filteredClubs = this.clubs.filter((club) =>
            club.category.includes(this.searchKeyword)
          );
        }
      }
    },
    // handlePageChange() {
    //   // 페이지 번호가 변경될 때 호출되는 메서드
    //   this.getBoards(this.pageNumber); // 현재 페이지 번호를 인자로 전달하여 데이터를 가져옴
    // },
  },
  created() {
    this.getClubs();
  },
  watch: {
    searchKeyword() {
      this.updateFilteredClubs(); // 검색어 변경 시 검색 결과 업데이트
    },
  },
};
</script>
  
<style scoped>
/* 클럽 아이템 스타일 */
.club-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #000000;
  transition: background-color 0.3s;
}

.club-item:hover {
  background-color: #222222;
}

/* 클럽 로고 이미지 스타일 */
.club-logo {
  width: 80px;
  height: 60px;
  margin-bottom: 10px;
}

/* 클럽 이름 스타일 */
.club-name {
  font-size: 20px;
  font-weight: bold;
  margin: 5px 0;
}

/* 클럽 설명 스타일 */
.club-description {
  font-size: 16px;
  color: #666;
  margin: 5px 0;
}
</style>