import React, { useEffect, useState } from "react";
import classService, { ClassEntity } from "../services/classService";
import userService, { User } from "../services/userService";

export default function Classes() {
  const [classes, setClasses] = useState<ClassEntity[]>([]);
  const [selectedClass, setSelectedClass] = useState<ClassEntity | null>(null);
  const [form, setForm] = useState({ name: "", teacherId: 0 });
  const [studentId, setStudentId] = useState(0);
  const [loading, setLoading] = useState(false);
  const [selectedUsers, setSelectedUsers] = useState<{ teacher?: User; students?: User[] } | null>(null);
  const [userMap, setUserMap] = useState<Map<number, User>>(new Map());

  // Lade alle Klassen + Users einmal
  const fetchClasses = async () => {
    setLoading(true);
    try {
      const data = await classService.getAll();

      // Lade alle Teacher und Students, um Map für schnelle Namensauflösung zu erstellen
      const allUserIds = new Set<number>();
      data.forEach(c => {
        allUserIds.add(c.teacherId);
        c.studentIds.forEach(id => allUserIds.add(id));
      });

      const usersArray = await Promise.all(Array.from(allUserIds).map(id => userService.getById(id)));
      const userMap = new Map<number, User>();
      usersArray.forEach(u => userMap.set(u.id, u));
      setUserMap(userMap);

      setClasses(data);
    } catch (err) {
      console.error("Error fetching classes:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchClasses();
  }, []);

  const handleViewUsers = async (c: ClassEntity) => {
    try {
      const teacher = userMap.get(c.teacherId);
      const students = c.studentIds.map(id => userMap.get(id)).filter(Boolean) as User[];
      setSelectedUsers({ teacher, students });
      setSelectedClass(c);
    } catch (err) {
      console.error("Error fetching users:", err);
    }
  };

  const handleAddClass = async () => {
    if (!form.name || !form.teacherId) return;
    const newClass = await classService.create({ name: form.name, teacherId: form.teacherId });
    setClasses([...classes, newClass]);
    setForm({ name: "", teacherId: 0 });
  };

  const handleUpdateClass = async () => {
    if (!selectedClass) return;
    const updated = await classService.update(selectedClass.id, selectedClass);
    setClasses(classes.map(c => (c.id === updated.id ? updated : c)));
    setSelectedClass(null);
  };

  const handleDeleteClass = async (id: number) => {
    await classService.delete(id);
    setClasses(classes.filter(c => c.id !== id));
    setSelectedClass(null);
  };

  const handleAddStudent = async () => {
    if (!selectedClass || !studentId) return;
    const updated = await classService.addStudent(selectedClass.id, studentId);
    setClasses(classes.map(c => (c.id === updated.id ? updated : c)));
    setSelectedClass(updated);
    setStudentId(0);
  };

  const handleRemoveStudent = async (sid: number) => {
    if (!selectedClass) return;
    const updated = await classService.removeStudent(selectedClass.id, sid);
    setClasses(classes.map(c => (c.id === updated.id ? updated : c)));
    setSelectedClass(updated);
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <h1 className="text-3xl font-bold text-center mb-8 text-gray-800">
        Class Management
      </h1>

      {/* Add Class Form */}
      <div className="max-w-3xl mx-auto bg-white p-6 rounded-2xl shadow-md mb-8 border border-gray-200">
        <h2 className="text-lg font-semibold mb-4">Add New Class</h2>
        <div className="grid md:grid-cols-3 gap-3">
          <input
            type="text"
            placeholder="Class Name"
            className="border p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={form.name}
            onChange={e => setForm({ ...form, name: e.target.value })}
          />
          <input
            type="number"
            placeholder="Teacher ID"
            className="border p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={form.teacherId || ""}
            onChange={e => setForm({ ...form, teacherId: Number(e.target.value) })}
          />
          <button
            onClick={handleAddClass}
            className="bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
          >
            Add Class
          </button>
        </div>
      </div>

      {/* Class Cards */}
      {loading ? (
        <p className="text-center text-gray-500">Loading classes...</p>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {classes.map(c => (
            <div key={c.id} className="bg-white p-5 rounded-2xl shadow-md border border-gray-200 hover:shadow-lg transition">
              <div className="flex justify-between items-start mb-3">
                <div>
                  <h3 className="text-lg font-semibold text-gray-800">{c.name}</h3>
                  <p className="text-sm text-gray-500">
                    Teacher: {userMap.get(c.teacherId)?.name || "Unknown"}
                  </p>
                  <p className="text-sm text-gray-500">
                    Students: {c.studentIds.map(id => userMap.get(id)?.name || id).join(", ")}
                  </p>
                </div>
                <div className="flex flex-col gap-2">
                  <button
                    onClick={() => setSelectedClass(c)}
                    className="text-blue-600 hover:underline text-sm"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDeleteClass(c.id)}
                    className="text-red-600 hover:underline text-sm"
                  >
                    Delete
                  </button>
                  <button
                    onClick={() => handleViewUsers(c)}
                    className="text-green-600 hover:underline text-sm"
                  >
                    View Users
                  </button>
                </div>
              </div>

              {/* Anzeige der Users */}
              {selectedUsers && selectedClass?.id === c.id && (
                <div className="border-t pt-3 mt-3">
                  <h4 className="font-semibold mb-2">Teacher</h4>
                  <p>{selectedUsers.teacher?.name} (ID: {selectedUsers.teacher?.id})</p>

                  <h4 className="font-semibold mt-3 mb-2">Students</h4>
                  <ul className="list-disc pl-5">
                    {selectedUsers.students?.map(s => (
                      <li key={s.id}>{s.name} (ID: {s.id})</li>
                    ))}
                  </ul>

                  <button
                    onClick={() => setSelectedUsers(null)}
                    className="mt-2 bg-gray-300 text-gray-800 px-3 py-1 rounded-lg hover:bg-gray-400 transition"
                  >
                    Close
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
