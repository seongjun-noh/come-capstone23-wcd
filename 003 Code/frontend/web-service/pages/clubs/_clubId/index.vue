<template>
  <div style="width: 100%">
    <div style="color: white; display: flex; flex-direction: row">
      <div style="width: 100%; padding:10px; padding-left: 0; padding-right: 30px;">
        <div class="d-flex">
          <NoticeCard />
          <ScheduleCard />
        </div>
        <div class="d-flex">
          <BoardCard />
          <CommunicationCard />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BoardCard from "@/components/ClubCard/BoardCard.vue";
import ScheduleCard from "@/components/ClubCard/ScheduleCard.vue";
import NoticeCard from "@/components/ClubCard/NoticeCard.vue";
import CommunicationCard from "@/components/ClubCard/CommunicationCard.vue";

export default {
  components: {
    BoardCard,
    ScheduleCard,
    NoticeCard,
    CommunicationCard,
  },
};
</script>