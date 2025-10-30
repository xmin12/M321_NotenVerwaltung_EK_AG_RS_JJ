import React, { useEffect, useState } from "react";
import api from "../api/api";

export default function Users() {
  const [users, setUsers] = useState<{id:number; name:string; email:string;}[]>([]);
  const [form, setForm] = useState({ name: "", email: "" });

  useEffect(() => {
    api.users.get("/").then(r => setUsers(r.data));
  }, []);

  const add = async () => {
    const res = await api.users.post("/", form);
    setUsers([...users, res.data]);
    setForm({ name: "", email: "" });
  };

  return (
    <div style={{padding:"1rem"}}>
      <h2>Benutzerverwaltung</h2>
      <input value={form.name} placeholder="Name" onChange={e=>setForm({...form,name:e.target.value})}/>
      <input value={form.email} placeholder="Email" onChange={e=>setForm({...form,email:e.target.value})}/>
      <button onClick={add}>Hinzufügen</button>
      <ul>{users.map(u=><li key={u.id}>{u.name} ({u.email})</li>)}</ul>
    </div>
  );
}
