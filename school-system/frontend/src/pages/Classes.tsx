import React, { useEffect, useState } from "react";
import api from "../api/api";

// --- NEW TYPES TO MATCH OUR DTOs ---
type UserDTO = {
  id: number;
  name: string;
  email: string;
  role: string;
};

type ClassDetailDTO = {
  id: number;
  name: string;
  teacher: UserDTO;
  students: UserDTO[];
};

// We still use the simple entity for the main list
type ClassEntity = {
  id: number;
  name: string;
  teacherId: number;
  studentIds: number[];
};
// --- END OF NEW TYPES ---

export default function Classes() {
  const [classes, setClasses] = useState<ClassEntity[]>([]);
  const [form, setForm] = useState({ name: "", teacherId: "" });
  
  // This state will now hold the full, rich DTO
  const [selectedClass, setSelectedClass] = useState<ClassDetailDTO | null>(null);
  
  const [studentId, setStudentId] = useState("");

  // Fetch the simple list on initial load
  useEffect(() => {
    fetchClasses();
  }, []);
  
  const fetchClasses = () => {
    api.classes.get("/").then((r: { data: ClassEntity[] }) => setClasses(r.data));
  };

  const addClass = async () => {
    await api.classes.post("/", {
      name: form.name,
      teacherId: Number(form.teacherId),
      studentIds: []
    });
    fetchClasses(); // Re-fetch the list to show the new class
    setForm({ name: "", teacherId: "" });
  };

  const deleteClass = async (id: number) => {
    await api.classes.delete(`/${id}`);
    setClasses(classes.filter((c) => c.id !== id));
    setSelectedClass(null);
  };

  // --- NEW FUNCTION TO HANDLE VIEWING DETAILS ---
  const handleViewDetails = async (classId: number) => {
    try {
      // Call the endpoint that now returns the rich DTO
      const res = await api.classes.get(`/${classId}`); 
      setSelectedClass(res.data);
    } catch (error) {
      console.error("Failed to fetch class details:", error);
      // Optionally, show an error message to the user
    }
  };
  
  // --- STUDENT MANAGEMENT ---
  const addStudent = async () => {
    if (!selectedClass || !studentId) return;
    await api.classes.post(`/${selectedClass.id}/students/${studentId}`);
    handleViewDetails(selectedClass.id); // Re-fetch the full details to update the view
    setStudentId("");
  };

  const removeStudent = async (sid: number) => {
    if (!selectedClass) return;
    await api.classes.delete(`/${selectedClass.id}/students/${sid}`);
    handleViewDetails(selectedClass.id); // Re-fetch the full details
  };

  return (
    <div style={{ padding: "1rem" }}>
      <h2>Klassenverwaltung</h2>

      {/* --- ADD CLASS FORM --- */}
      <div style={{ marginBottom: "1rem" }}>
        <input value={form.name} placeholder="Klassenname" onChange={(e) => setForm({ ...form, name: e.target.value })} />
        <input value={form.teacherId} placeholder="Lehrer-ID" type="number" onChange={(e) => setForm({ ...form, teacherId: e.target.value })} />
        <button onClick={addClass}>Klasse hinzufügen</button>
      </div>

      {/* --- CLASS LIST --- */}
      <ul>
        {classes.map((c) => (
          <li key={c.id} style={{ marginBottom: "1rem" }}>
            <b>{c.name}</b> (Lehrer-ID: {c.teacherId})
            <button style={{ marginLeft: "1rem" }} onClick={() => handleViewDetails(c.id)}>Details / Bearbeiten</button>
            <button style={{ marginLeft: "0.5rem" }} onClick={() => deleteClass(c.id)}>Löschen</button>
          </li>
        ))}
      </ul>

      {/* --- DETAILS/EDIT VIEW (Updated to show rich data) --- */}
      {selectedClass && (
        <div style={{ border: "1px solid #ccc", padding: "1rem", marginTop: "1rem" }}>
          <h3>Details für Klasse: {selectedClass.name}</h3>
          <div>
            <h4>Lehrer</h4>
<p>{selectedClass.teacher.name} (ID: {selectedClass.teacher.id}, Email: {selectedClass.teacher.email})</p>          </div>
          
          <div style={{ marginTop: "1rem" }}>
            <h4>Schülerliste</h4>
            {selectedClass.students.length > 0 ? (
              <ul>
                {selectedClass.students.map((student) => (
                  <li key={student.id}>
                    {student.name} (ID: {student.id})
                    <button style={{marginLeft: "0.5rem"}} onClick={() => removeStudent(student.id)}>Entfernen</button>
                  </li>
                ))}
              </ul>
            ) : <p>Keine Schüler in dieser Klasse.</p>}
          </div>

          <div style={{ marginTop: "1rem" }}>
            <h4>Neuen Schüler hinzufügen</h4>
            <input value={studentId} onChange={(e) => setStudentId(e.target.value)} placeholder="Schüler-ID" type="number" />
            <button onClick={addStudent}>Hinzufügen</button>
          </div>
          
          <button onClick={() => setSelectedClass(null)} style={{ marginTop: "1rem" }}>Schließen</button>
        </div>
      )}
    </div>
  );
}