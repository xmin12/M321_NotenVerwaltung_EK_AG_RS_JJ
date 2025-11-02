import api from "../api/api";

export interface User {
  id: number;
  name: string;
  email: string;
  password: string;
  role: "ADMIN" | "TEACHER" | "STUDENT";
}

export interface CreateUserDto {
  name: string;
  email: string;
  password: string;
  role: "ADMIN" | "TEACHER" | "STUDENT";
}

class UserService {
  async getAll() {
    const res = await api.users.get(""); 
    return res.data;
  }

  async getById(id: number) {
    const res = await api.users.get(`/${id}`);
    return res.data;
  }

  async getByRole(role: string) {
    const res = await api.users.get(`/role/${role}`);
    return res.data;
  }

  async create(user: CreateUserDto) {
    const res = await api.users.post("", user);
    return res.data;
  }

  async update(id: number, user: CreateUserDto) {
    const res = await api.users.put(`/${id}`, user);
    return res.data;
  }

  async delete(id: number) {
    await api.users.delete(`/${id}`);
  }
}

export default new UserService();
