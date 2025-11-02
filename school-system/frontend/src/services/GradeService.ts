import api from "../api/api";

const gradeApi = api.grades;

export interface Grade {
  id: number;
  studentId: number;
  classId: number;
  subject: string;
  value: number;
  date: string;
}

export interface CreateGradeDto {
  studentId: number;
  classId: number;
  subject: string;
  value: number;
  date: string;
}

class GradeService {
  async getAll() {
    const res = await gradeApi.get<Grade[]>("");
    return res.data;
  }

  async getByStudent(studentId: number) {
    const res = await gradeApi.get<Grade[]>(`/student/${studentId}`);
    return res.data;
  }

  async getByClass(classId: number) {
    const res = await gradeApi.get<Grade[]>(`/class/${classId}`);
    return res.data;
  }

  async create(grade: CreateGradeDto) {
    const res = await gradeApi.post<Grade>("", grade);
    return res.data;
  }

  async update(id: number, updatedGrade: Partial<CreateGradeDto>) {
    const res = await gradeApi.put<Grade>(`/${id}`, updatedGrade);
    return res.data;
  }

  async delete(id: number) {
    await gradeApi.delete(`/${id}`);
  }
}


const gradeService = new GradeService();

export default gradeService;
