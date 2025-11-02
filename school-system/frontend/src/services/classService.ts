import api from "../api/api";

export interface ClassEntity {
  id: number;
  name: string;
  teacherId: number;
  studentIds: number[];
}

interface CreateClassDto {
  name: string;
  teacherId: number;
  studentIds?: number[];
}

class ClassService {
  async getAll(): Promise<ClassEntity[]> {
    const res = await api.classes.get("");
    return res.data;
  }

  async getById(id: number): Promise<ClassEntity> {
    const res = await api.classes.get(`/${id}`);
    return res.data;
  }

  async create(newClass: CreateClassDto): Promise<ClassEntity> {
    const res = await api.classes.post("", newClass);
    return res.data;
  }

  async update(id: number, updatedClass: Partial<ClassEntity>): Promise<ClassEntity> {
    const res = await api.classes.put(`/${id}`, updatedClass);
    return res.data;
  }

  async delete(id: number): Promise<void> {
    await api.classes.delete(`/${id}`);
  }

  async addStudent(classId: number, studentId: number): Promise<ClassEntity> {
    const res = await api.classes.post(`/${classId}/students/${studentId}`);
    return res.data;
  }

  async removeStudent(classId: number, studentId: number): Promise<ClassEntity> {
    const res = await api.classes.delete(`/${classId}/students/${studentId}`);
    return res.data;
  }

  async changeTeacher(classId: number, teacherId: number): Promise<ClassEntity> {
    const res = await api.classes.put(`/${classId}/teacher/${teacherId}`);
    return res.data;
  }
}

const classService = new ClassService();
export default classService;
