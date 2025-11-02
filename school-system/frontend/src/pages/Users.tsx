import React, { useEffect, useState } from "react";
import userService, { User, CreateUserDto } from "../services/userService";

const Users: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [newUser, setNewUser] = useState<CreateUserDto>({
    name: "",
    email: "",
    password: "",
    role: "STUDENT",
  });
  const [loading, setLoading] = useState(false);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const data = await userService.getAll();
      setUsers(data);
    } catch (err) {
      console.error("Fehler beim Laden der Benutzer:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleCreate = async () => {
    if (!newUser.name || !newUser.email || !newUser.password) {
      alert("Bitte alle Felder ausfüllen!");
      return;
    }
    try {
      await userService.create(newUser);
      setNewUser({ name: "", email: "", password: "", role: "STUDENT" });
      fetchUsers();
    } catch (err) {
      console.error("Fehler beim Erstellen des Benutzers:", err);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Benutzer wirklich löschen?")) return;
    try {
      await userService.delete(id);
      fetchUsers();
    } catch (err) {
      console.error("Fehler beim Löschen:", err);
    }
  };

  return (
    <div className="p-6 max-w-3xl mx-auto">
      <h1 className="text-2xl font-bold mb-4 text-center">Benutzerverwaltung</h1>

      {/* Neues Benutzerformular */}
      <div className="bg-gray-100 p-4 rounded-lg shadow mb-6">
        <h2 className="font-semibold mb-2">Neuen Benutzer hinzufügen</h2>
        <div className="grid grid-cols-2 gap-3">
          <input
            type="text"
            placeholder="Name"
            className="border p-2 rounded"
            value={newUser.name}
            onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
          />
          <input
            type="email"
            placeholder="E-Mail"
            className="border p-2 rounded"
            value={newUser.email}
            onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
          />
          <input
            type="password"
            placeholder="Passwort"
            className="border p-2 rounded"
            value={newUser.password}
            onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
          />
          <select
            className="border p-2 rounded"
            value={newUser.role}
            onChange={(e) =>
              setNewUser({ ...newUser, role: e.target.value as CreateUserDto["role"] })
            }
          >
            <option value="ADMIN">ADMIN</option>
            <option value="TEACHER">TEACHER</option>
            <option value="STUDENT">STUDENT</option>
          </select>
        </div>
        <button
          onClick={handleCreate}
          className="mt-3 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Hinzufügen
        </button>
      </div>

      {/* Benutzerliste */}
      <h2 className="text-xl font-semibold mb-2">Benutzerliste</h2>
      {loading ? (
        <p>Lade Benutzer...</p>
      ) : users.length === 0 ? (
        <p>Keine Benutzer gefunden.</p>
      ) : (
        <table className="w-full border-collapse border border-gray-300">
          <thead className="bg-gray-200">
            <tr>
              <th className="border p-2 text-left">ID</th>
              <th className="border p-2 text-left">Name</th>
              <th className="border p-2 text-left">E-Mail</th>
              <th className="border p-2 text-left">Rolle</th>
              <th className="border p-2 text-center">Aktionen</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className="hover:bg-gray-50">
                <td className="border p-2">{user.id}</td>
                <td className="border p-2">{user.name}</td>
                <td className="border p-2">{user.email}</td>
                <td className="border p-2">{user.role}</td>
                <td className="border p-2 text-center">
                  <button
                    onClick={() => handleDelete(user.id)}
                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                  >
                    Löschen
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default Users;
