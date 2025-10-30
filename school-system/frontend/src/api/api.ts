import axios from "axios";

const api = {
  users: axios.create({ baseURL: "http://localhost:8081/api/users" }),
  classes: axios.create({ baseURL: "http://localhost:8082/api/classes" }),
  grades: axios.create({ baseURL: "http://localhost:8083/api/grades" }),
  stats: axios.create({ baseURL: "http://localhost:8084/api/stats" }),
};

export default api;
