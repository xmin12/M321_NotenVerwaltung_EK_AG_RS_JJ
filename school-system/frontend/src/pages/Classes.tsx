import React, { useEffect, useState } from "react";
import classService, { ClassEntity } from "../services/classService";
import userService, { User } from "../services/userService";

export default function Classes() {
  const [classes, setClasses] = useState<ClassEntity[]>([]);
  const [selectedClass, setSelectedClass] = useState<ClassEntity | null>(null);
  const [form, setForm] = useState({ name: "", teacherId: 0 });
  const [selectedStudentIds, setSelectedStudentIds] = useState<number[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedUsers, setSelectedUsers] = useState<{ teacher?: User; students?: User[] } | null>(null);
  const [userMap, setUserMap] = useState<Map<number, User>>(new Map());
  const [allStudents, setAllStudents] = useState<User[]>([]); 

  const fetchClasses = async () => {
    setLoading(true);
    try {
      const data = await classService.getAll();
      const allUserIds = new Set<number>();
      data.forEach(c => {
        allUserIds.add(c.teacherId);
        c.studentIds.forEach(id => allUserIds.add(id));
      });
      const usersArray = await Promise.all(Array.from(allUserIds).map(id => userService.getById(id)));
      const userMap = new Map<number, User>();
      usersArray.forEach(u => userMap.set(u.id, u));
      setUserMap(userMap);

      const students = await userService.getByRole("STUDENT");
      setAllStudents(students);

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

  const handleViewUsers = (c: ClassEntity) => {
    const teacher = userMap.get(c.teacherId);
    const students = c.studentIds.map(id => userMap.get(id)).filter(Boolean) as User[];
    setSelectedUsers({ teacher, students });
    setSelectedClass(c);
    setSelectedStudentIds([]); 
  };

  const handleAddStudents = async () => {
    if (!selectedClass || selectedStudentIds.length === 0) return;

    let updatedClass = selectedClass;
    for (const sid of selectedStudentIds) {
      updatedClass = await classService.addStudent(selectedClass.id, sid);
    }
    setClasses(classes.map(c => (c.id === updatedClass.id ? updatedClass : c)));
    setSelectedClass(updatedClass);
    setSelectedStudentIds([]);
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <h1 className="text-3xl font-bold text-center mb-8 text-gray-800">Class Management</h1>

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
                  <button onClick={() => handleViewUsers(c)} className="text-green-600 hover:underline text-sm">View Users</button>
                </div>
              </div>

              {selectedUsers && selectedClass?.id === c.id && (
                <div className="border-t pt-3 mt-3">
                  <h4 className="font-semibold mb-2">Teacher</h4>
                  <p>{selectedUsers.teacher?.name} (ID: {selectedUsers.teacher?.id})</p>

                  <h4 className="font-semibold mt-3 mb-2">Students</h4>
                  <ul className="list-disc pl-5 mb-2">
                    {selectedUsers.students?.map(s => (
                      <li key={s.id}>{s.name} (ID: {s.id})</li>
                    ))}
                  </ul>

                  <h4 className="font-semibold mt-3 mb-2">Add Students</h4>
                  <select
                    multiple
                    value={selectedStudentIds.map(String)}
                    onChange={e =>
                      setSelectedStudentIds(Array.from(e.target.selectedOptions, option => Number(option.value)))
                    }
                    className="border p-2 rounded-lg w-full mb-2 focus:ring-2 focus:ring-blue-400"
                  >
                    {allStudents
                      .filter(s => !selectedClass.studentIds.includes(s.id))
                      .map(s => (
                        <option key={s.id} value={s.id}>
                          {s.name} (ID: {s.id})
                        </option>
                      ))}
                  </select>
                  <button onClick={handleAddStudents} className="bg-blue-600 text-white px-4 py-1 rounded-lg hover:bg-blue-700 transition">
                    Add Selected Students
                  </button>

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
