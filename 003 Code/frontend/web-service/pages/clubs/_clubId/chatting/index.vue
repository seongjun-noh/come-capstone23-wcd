<template>
  <div>
    <div
      style="
        background-color: rgb(243, 94, 49);
        height: 65px;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
      "
    >
      <h3>채팅방</h3>
    </div>
    <div style="height: 600px">
      <ul>
        <li
          v-for="testmessage in testMessages"
          :key="testmessage.id"
          style="color: black"
        >
          {{ testmessage.sender }}: {{ testmessage.message }} [{{
            testmessage.sendTime
          }}]
        </li>
        <li v-for="message in messages" :key="message.id" style="color: black">
          {{ message.sender }}: {{ message.message }} [{{ message.sendTime }}]
        </li>
      </ul>
    </div>
    <div style="height: 50px; background-color: gray">
      <v-btn>알림 활성화</v-btn>
    </div>
    <div style="height: 110px; padding: 5px">
      <textarea
        v-model="message"
        placeholder="메시지를 입력하세요"
        @keyup.enter="sendMessage"
        style="
          flex: 1;
          padding: 10px;
          border-radius: 5px;
          height: 100%;
          width: 100%;
          resize: none;
        "
      ></textarea>
    </div>
  </div>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default {
  data() {
    return {
      clubId: null,
      message: "",
      messages: [],
      stompClient: null,
      testMessages: [],
      clubName: null,
      userName: null,
    };
  },
  layout(context) {
    return "chatting";
  },
  methods: {
    connect() {
      const socket = new SockJS("https://wcd.kro.kr/api/chatting-service/ws");
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, this.onConnected, this.onError);
    },
    onConnected() {
      this.stompClient.subscribe(`/topic/${this.clubId}`,this.onMessageReceived);
    },
    onError(error) {
      console.error(error);
    },
    onMessageReceived(payload) {
      const parseMessage = JSON.parse(payload.body);
      this.messages.push(parseMessage);
    },
    sendMessage() {
      if (!this.message.trim()) {
        return;
      }

      let chat = {
        clubId: this.clubId,
        senderId: sessionStorage.getItem("user_id"),
        message: this.message,
      };

      if (this.stompClient) {
        this.stompClient.send("/app/chat/send", {}, JSON.stringify(chat));

        this.sendAlarm();

        this.message = "";
      } else {
        console.error("WebSocket connection is not established.");
      }
    },

    sendAlarm() {
      const messageData = {
        clubName: this.clubName,
        userName: "유저이름",
        chatMessage: this.message,
        topic: this.clubId,
      };
      fetch("http://211.115.222.246:5004/sendChatMessage", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(messageData),
      })
        .then((response) => {
          console.log("데이터 전송 성공:", response);
        })
        .catch((error) => {
          console.error("데이터 전송 중 오류:", error);
        });
    },

    async getMessage() {
      try {
        const access_token = this.$store.state.access_token;
        const config = {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${access_token}`,
          },
          params: {
            clubId: this.clubId,
            page: 0,
            size: 10,
          },
        };
        await this.$axios
          .get(`https://wcd.kro.kr/api/chatting-service/chat`, config)
          .then((res) => {
            console.log(res);
            console.log(res.data.content);
            this.testMessages = res.data.content.reverse();
            console.log(this.testMessages);
          });
      } catch (err) {
        console.error("err", err);
      }
    },
    async getClubInfo() {
      try {
        const access_token = this.$store.state.access_token;
        const config = {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${access_token}`,
          },
        };
        await this.$axios
          .get(`/club-service/clubs/${this.$route.params.clubId}`, config)
          .then((res) => {
            console.log(res);
            this.clubName = res.data.clubName;
          });
      } catch (err) {
        console.log(err);
      }
    },
  },
  async created() {
    this.clubId = this.$route.params.clubId;
    this.getClubInfo();
    this.connect();
    this.getMessage();
  },
};
</script>
