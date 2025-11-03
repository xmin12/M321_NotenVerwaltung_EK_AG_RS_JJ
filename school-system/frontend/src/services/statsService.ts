import api from "../api/api";

export interface Stats {
id: number;
studentId: number;
subject: string;
highestGrade: number;
lowestGrade: number;
averageGrade: number;
}

class statsService {
async getAll() {
const res = await api.stats.get("/");
return res.data as Stats[];
}

async getByStudent(studentId: number) {
const res = await api.stats.get(`/student/${studentId}`);
return res.data as Stats[];
}

async getBySubject(subject: string) {
const res = await api.stats.get(`/subject/${subject}`);
return res.data as Stats[];
}

async refresh() {
const res = await api.stats.post("/refresh");
return res.data as Stats[];
}
}

export default new statsService();