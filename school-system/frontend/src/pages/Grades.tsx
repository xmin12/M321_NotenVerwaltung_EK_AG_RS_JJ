import React, { useEffect, useState, useCallback } from "react";
import GradeService, { Grade, CreateGradeDto } from "../services/GradeService";

export default function GradePage() {
  const [grades, setGrades] = useState<Grade[]>([]);
  const [filterStudentId, setFilterStudentId] = useState("");
  const [filterClassId, setFilterClassId] = useState("");
  const [form, setForm] = useState({ studentId: "", classId: "", subject: "", value: "" });
  const [editingGrade, setEditingGrade] = useState<Grade | null>(null);

  const fetchGrades = useCallback(async () => {
    let data: Grade[] = [];
    try {
      if (filterStudentId) {
        data = await GradeService.getByStudent(Number(filterStudentId));
      } else if (filterClassId) {
        data = await GradeService.getByClass(Number(filterClassId));
      } else {
        data = await GradeService.getAll();
      }
      setGrades(data);
    } catch (err) {
      console.error("Failed to fetch grades:", err);
      alert("Error fetching grades");
    }
  }, [filterStudentId, filterClassId]);

  useEffect(() => {
    fetchGrades();
  }, [fetchGrades]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const newGrade: CreateGradeDto = {
        studentId: Number(form.studentId),
        classId: Number(form.classId),
        subject: form.subject,
        value: Number(form.value),
        date: new Date().toISOString().split("T")[0],
      };
      const created = await GradeService.create(newGrade);
      setGrades([...grades, created]);
      setForm({ studentId: "", classId: "", subject: "", value: "" });
    } catch (err) {
      console.error("Failed to add grade:", err);
      alert("Error adding grade");
    }
  };

  const handleDelete = async (id: number) => {
<<<<<<< HEAD
    if (!window.confirm("Delete this grade?")) return;
=======
    if (!globalThis.confirm("Delete this grade?")) return;
>>>>>>> 6ca0f4e170a7ad1c7b4c51b8150dd1dcc1a307b4
    try {
      await GradeService.delete(id);
      setGrades(grades.filter(g => g.id !== id));
    } catch (err) {
      console.error("Failed to delete grade:", err);
      alert("Error deleting grade");
    }
  };

  const startEdit = (grade: Grade) => {
    setEditingGrade({ ...grade });
  };

  const handleEditChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!editingGrade) return;
    setEditingGrade({ ...editingGrade, [e.target.name]: e.target.value });
  };

  const handleUpdate = async () => {
    if (!editingGrade) return;
    try {
      const updated = await GradeService.update(editingGrade.id, {
        studentId: Number(editingGrade.studentId),
        classId: Number(editingGrade.classId),
        subject: editingGrade.subject,
        value: Number(editingGrade.value),
        date: editingGrade.date,
      });
      setGrades(grades.map(g => (g.id === updated.id ? updated : g)));
      setEditingGrade(null);
    } catch (err) {
      console.error("Failed to update grade:", err);
      alert("Error updating grade");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Notenverwaltung</h2>

      <div style={{ marginBottom: "20px" }}>
        <input
          placeholder="Filter by Student ID"
          value={filterStudentId}
          onChange={e => setFilterStudentId(e.target.value)}
          style={{ marginRight: "10px" }}
        />
        <input
          placeholder="Filter by Class ID"
          value={filterClassId}
          onChange={e => setFilterClassId(e.target.value)}
          style={{ marginRight: "10px" }}
        />
        <button onClick={fetchGrades}>Filter</button>
        <button
          onClick={() => {
            setFilterStudentId("");
            setFilterClassId("");
            fetchGrades();
          }}
          style={{ marginLeft: "10px" }}
        >
          Clear
        </button>
      </div>

      <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
        <input
          name="studentId"
          placeholder="Student ID"
          value={form.studentId}
          onChange={handleChange}
          required
          style={{ marginRight: "10px" }}
        />
        <input
          name="classId"
          placeholder="Class ID"
          value={form.classId}
          onChange={handleChange}
          required
          style={{ marginRight: "10px" }}
        />
        <input
          name="subject"
          placeholder="Subject"
          value={form.subject}
          onChange={handleChange}
          required
          style={{ marginRight: "10px" }}
        />
        <input
          name="value"
          placeholder="Grade"
          type="number"
          step="0.1"
          value={form.value}
          onChange={handleChange}
          required
          style={{ marginRight: "10px" }}
        />
        <button type="submit">Add Grade</button>
      </form>

      <table border={1} cellPadding={5}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Student ID</th>
            <th>Class ID</th>
            <th>Subject</th>
            <th>Value</th>
            <th>Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {grades.map(g => (
            <tr key={g.id}>
              <td>{g.id}</td>
              {editingGrade && editingGrade.id === g.id ? (
                <>
                  <td>
                    <input
                      name="studentId"
                      value={editingGrade.studentId}
                      onChange={handleEditChange}
                    />
                  </td>
                  <td>
                    <input
                      name="classId"
                      value={editingGrade.classId}
                      onChange={handleEditChange}
                    />
                  </td>
                  <td>
                    <input
                      name="subject"
                      value={editingGrade.subject}
                      onChange={handleEditChange}
                    />
                  </td>
                  <td>
                    <input
                      name="value"
                      type="number"
                      step="0.1"
                      value={editingGrade.value}
                      onChange={handleEditChange}
                    />
                  </td>
                  <td>{editingGrade.date}</td>
                  <td>
                    <button onClick={handleUpdate}>Save</button>
                    <button onClick={() => setEditingGrade(null)}>Cancel</button>
                  </td>
                </>
              ) : (
                <>
                  <td>{g.studentId}</td>
                  <td>{g.classId}</td>
                  <td>{g.subject}</td>
                  <td>{g.value}</td>
                  <td>{g.date}</td>
                  <td>
                    <button onClick={() => startEdit(g)}>Edit</button>
                    <button onClick={() => handleDelete(g.id)}>Delete</button>
                  </td>
                </>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
