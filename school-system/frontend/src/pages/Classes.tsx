
import React, { useEffect, useState } from "react";
import api from "../api/api";

type ClassEntity = {
  id: number;
  name: string;
  teacherId: number;
  studentIds: number[];
};

export default function Classes() {
  const [classes, setClasses] = useState<ClassEntity[]>([]);
  const [form, setForm] = useState({ name: "", teacherId: "" });
  const [selectedClass, setSelectedClass] = useState<ClassEntity | null>(null);
  const [studentId, setStudentId] = useState("");

  useEffect(() => {
    api.classes.get("/").then((r: { data: ClassEntity[] }) => setClasses(r.data));
  }, []);

  const addClass = async () => {
    const res = await api.classes.post("/", {
      name: form.name,
      teacherId: Number(form.teacherId),
      studentIds: []
    });
    setClasses([...classes, res.data]);
    setForm({ name: "", teacherId: "" });
  };

  const updateClass = async () => {
    if (!selectedClass) return;
    const res = await api.classes.put(`/${selectedClass.id}`, {
      name: selectedClass.name,
      teacherId: selectedClass.teacherId,
      studentIds: selectedClass.studentIds
    });
    setClasses(classes.map((c: ClassEntity) => c.id === selectedClass.id ? res.data : c));
    setSelectedClass(null);
  };

  const deleteClass = async (id: number) => {
    await api.classes.delete(`/${id}`);
    setClasses(classes.filter((c: ClassEntity) => c.id !== id));
    setSelectedClass(null);
  };

  const addStudent = async () => {
    if (!selectedClass || !studentId) return;
    const res = await api.classes.post(`/${selectedClass.id}/students/${studentId}`);
    setClasses(classes.map((c: ClassEntity) => c.id === selectedClass.id ? res.data : c));
    setSelectedClass(res.data);
    setStudentId("");
  };

  const removeStudent = async (sid: number) => {
    if (!selectedClass) return;
    const res = await api.classes.delete(`/${selectedClass.id}/students/${sid}`);
    setClasses(classes.map((c: ClassEntity) => c.id === selectedClass.id ? res.data : c));
    setSelectedClass(res.data);
  };

  return (
    <div style={{padding:"1rem"}}>
      <h2>Klassenverwaltung</h2>
      <div style={{marginBottom:"1rem"}}>
        <input value={form.name} placeholder="Klassenname" onChange={(e: React.ChangeEvent<HTMLInputElement>)=>setForm({...form,name:e.target.value})}/>
        <input value={form.teacherId} placeholder="Lehrer-ID" type="number" onChange={(e: React.ChangeEvent<HTMLInputElement>)=>setForm({...form,teacherId:e.target.value})}/>
        <button onClick={addClass}>Klasse hinzufügen</button>
      </div>
      <ul>
        {classes.map((c: ClassEntity)=>(
          <li key={c.id} style={{marginBottom:"1rem"}}>
            <b>{c.name}</b> (Lehrer: {c.teacherId})
            <button style={{marginLeft:"1rem"}} onClick={()=>setSelectedClass(c)}>Bearbeiten</button>
            <button style={{marginLeft:"0.5rem"}} onClick={()=>deleteClass(c.id)}>Löschen</button>
            <div>Schüler: {c.studentIds.join(", ")}</div>
          </li>
        ))}
      </ul>
      {selectedClass && (
        <div style={{border:"1px solid #ccc",padding:"1rem",marginTop:"1rem"}}>
          <h3>Klasse bearbeiten: {selectedClass.name}</h3>
          <input value={selectedClass.name} onChange={(e: React.ChangeEvent<HTMLInputElement>)=>setSelectedClass({...selectedClass,name:e.target.value})} placeholder="Name"/>
          <input value={selectedClass.teacherId} type="number" onChange={(e: React.ChangeEvent<HTMLInputElement>)=>setSelectedClass({...selectedClass,teacherId:Number(e.target.value)})} placeholder="Lehrer-ID"/>
          <button onClick={updateClass}>Speichern</button>
          <button onClick={()=>setSelectedClass(null)} style={{marginLeft:"0.5rem"}}>Abbrechen</button>
          <div style={{marginTop:"1rem"}}>
            <h4>Schüler</h4>
            <input value={studentId} onChange={(e: React.ChangeEvent<HTMLInputElement>)=>setStudentId(e.target.value)} placeholder="Schüler-ID" type="number"/>
            <button onClick={addStudent}>Schüler hinzufügen</button>
            <ul>
              {selectedClass.studentIds.map((sid: number)=>(
                <li key={sid}>
                  {sid} <button onClick={()=>removeStudent(sid)}>Entfernen</button>
                </li>
              ))}
            </ul>
          </div>
        </div>
      )}
    </div>
  );
}
