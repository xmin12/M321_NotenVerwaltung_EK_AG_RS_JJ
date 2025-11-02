import React, { useEffect, useState } from "react";
import userService, { User, CreateUserDto } from "../services/userService";
import gradeService, { Grade, CreateGradeDto } from "../services/GradeService";

interface UserWithGrades extends User {
  grades: Grade[];
}

const UsersPage: React.FC = () => {
  const [users, setUsers] = useState<UserWithGrades[]>([]);
  const [loading, setLoading] = useState(false);

  const [newUser, setNewUser] = useState<CreateUserDto>({
    name: "",
    email: "",
    password: "",
    role: "STUDENT",
  });

  const [newGrade, setNewGrade] = useState<CreateGradeDto>({
    studentId: 0,
    classId: 0,
    subject: "",
    value: 0,
    date: "",
  });

  const [roleFilter, setRoleFilter] = useState<"ALL" | "ADMIN" | "TEACHER" | "STUDENT">(
    "ALL"
  );

  const fetchUsersWithGrades = async () => {
    try {
      setLoading(true);
      const allUsers = await userService.getAll();
      const usersWithGrades: UserWithGrades[] = await Promise.all(
        allUsers.map(async (user: User) => {
          const grades = await gradeService.getByStudent(user.id).catch(() => []);
          return { ...user, grades };
        })
      );
      setUsers(usersWithGrades);
    } catch (err) {
      console.error("Error fetching users:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsersWithGrades();
  }, []);

  const handleCreateUser = async () => {
    if (!newUser.name || !newUser.email || !newUser.password) {
      alert("Please fill in all fields!");
      return;
    }
    await userService.create(newUser);
    setNewUser({ name: "", email: "", password: "", role: "STUDENT" });
    fetchUsersWithGrades();
  };

  const handleDeleteUser = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this user?")) return;
    await userService.delete(id);
    fetchUsersWithGrades();
  };

  const handleAddGrade = async (userId: number) => {
    if (!newGrade.subject || !newGrade.value || !newGrade.date) {
      alert("Please fill in all grade fields!");
      return;
    }

    const gradeToAdd = {
      ...newGrade,
      studentId: userId,
      classId: newGrade.classId || 1,
    };

    await gradeService.create(gradeToAdd);
    setNewGrade({ studentId: 0, classId: 0, subject: "", value: 0, date: "" });
    fetchUsersWithGrades();
  };

  const handleDeleteGrade = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this grade?")) return;
    await gradeService.delete(id);
    fetchUsersWithGrades();
  };

  // Filter users by role
  const filteredUsers =
    roleFilter === "ALL" ? users : users.filter((u) => u.role === roleFilter);

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <h1 className="text-3xl font-bold text-center mb-8">User & Grade Management</h1>

      {/* Filter by role */}
      <div className="max-w-3xl mx-auto mb-6 flex justify-between items-center">
        <label className="mr-2 font-medium">Filter by role:</label>
        <select
          className="border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
          value={roleFilter}
          onChange={(e) => setRoleFilter(e.target.value as any)}
        >
          <option value="ALL">All</option>
          <option value="STUDENT">Students</option>
          <option value="TEACHER">Teachers</option>
          <option value="ADMIN">Admins</option>
        </select>
      </div>

      {/* Add new user */}
      <div className="max-w-3xl mx-auto bg-white p-6 rounded-xl shadow mb-10 border border-gray-200">
        <h2 className="text-lg font-semibold mb-4">Add New User</h2>
        <div className="grid md:grid-cols-4 gap-3">
          <input
            type="text"
            placeholder="Name"
            className="border p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={newUser.name}
            onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
          />
          <input
            type="email"
            placeholder="Email"
            className="border p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={newUser.email}
            onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
          />
          <input
            type="password"
            placeholder="Password"
            className="border p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={newUser.password}
            onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
          />
          <select
            className="border p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={newUser.role}
            onChange={(e) =>
              setNewUser({ ...newUser, role: e.target.value as CreateUserDto["role"] })
            }
          >
            <option value="ADMIN">Admin</option>
            <option value="TEACHER">Teacher</option>
            <option value="STUDENT">Student</option>
          </select>
        </div>
        <button
          onClick={handleCreateUser}
          className="mt-4 w-full bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition"
        >
          Add User
        </button>
      </div>

      {/* Users list */}
      {loading ? (
        <p className="text-center py-10">Loading users...</p>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredUsers.map((user) => (
            <div
              key={user.id}
              className="bg-white p-5 rounded-xl shadow border border-gray-200 hover:shadow-lg transition"
            >
              <div className="flex justify-between items-start mb-3">
                <div>
                  <h3 className="text-lg font-semibold">{user.name}</h3>
                  <p className="text-sm text-gray-500">{user.email}</p>
                  <span className="text-xs px-2 py-1 rounded-full bg-blue-100 text-blue-800 font-medium">
                    {user.role}
                  </span>
                </div>
                <button
                  onClick={() => handleDeleteUser(user.id)}
                  className="text-red-500 hover:text-red-700 font-bold text-sm"
                >
                  Delete
                </button>
              </div>

              <div className="border-t pt-3">
                <h4 className="font-semibold text-gray-700 mb-2">Grades:</h4>

                {user.grades.length > 0 ? (
                  <ul className="space-y-1">
                    {user.grades.map((g) => (
                      <li
                        key={g.id}
                        className="flex justify-between items-center bg-gray-100 px-3 py-1.5 rounded-lg"
                      >
                        <span>
                          <strong>{g.subject}</strong>: {g.value} ({g.date})
                        </span>
                        <button
                          onClick={() => handleDeleteGrade(g.id)}
                          className="text-red-500 hover:text-red-700 text-sm"
                        >
                          Delete
                        </button>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p className="text-gray-400 text-sm">No grades available.</p>
                )}

                {user.role === "STUDENT" && (
                  <div className="mt-4">
                    <h5 className="text-sm font-medium mb-1">Add New Grade:</h5>
                    <div className="grid grid-cols-4 gap-2">
                      <input
                        type="text"
                        placeholder="Subject"
                        className="border p-1.5 rounded-lg text-sm focus:ring-2 focus:ring-green-400"
                        value={newGrade.subject}
                        onChange={(e) =>
                          setNewGrade({ ...newGrade, subject: e.target.value })
                        }
                      />
                      <input
                        type="number"
                        placeholder="Grade"
                        className="border p-1.5 rounded-lg text-sm focus:ring-2 focus:ring-green-400"
                        value={newGrade.value}
                        onChange={(e) =>
                          setNewGrade({ ...newGrade, value: parseFloat(e.target.value) })
                        }
                      />
                      <input
                        type="date"
                        className="border p-1.5 rounded-lg text-sm focus:ring-2 focus:ring-green-400"
                        value={newGrade.date}
                        onChange={(e) =>
                          setNewGrade({ ...newGrade, date: e.target.value })
                        }
                      />
                      <button
                        onClick={() => handleAddGrade(user.id)}
                        className="bg-green-600 text-white rounded-lg hover:bg-green-700 text-sm font-medium"
                      >
                        Add
                      </button>
                    </div>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default UsersPage;
