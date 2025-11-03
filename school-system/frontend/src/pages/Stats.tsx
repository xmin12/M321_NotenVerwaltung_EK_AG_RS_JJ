import React, { useEffect, useState } from "react";
import statsService, { Stats } from "../services/statsService";

const StatsPage: React.FC = () => {
const [stats, setStats] = useState<Stats[]>([]);
const [loading, setLoading] = useState(false);
const [studentFilter, setStudentFilter] = useState<number | "">("");
const [subjectFilter, setSubjectFilter] = useState<string>("");

const fetchStats = async () => {
try {
setLoading(true);
let data: Stats[] = [];
if (studentFilter && subjectFilter) {
const studentStats = await statsService.getByStudent(studentFilter);
data = studentStats.filter((s) => s.subject === subjectFilter);
} else if (studentFilter) {
data = await statsService.getByStudent(studentFilter);
} else if (subjectFilter) {
data = await statsService.getBySubject(subjectFilter);
} else {
data = await statsService.getAll();
}
setStats(data);
} catch (err) {
console.error("Fehler beim Laden der Statistiken:", err);
} finally {
setLoading(false);
}
};

useEffect(() => {
fetchStats();
}, []);

const handleRefresh = async () => {
try {
setLoading(true);
const data = await statsService.refresh();
setStats(data);
} catch (err) {
console.error("Fehler beim Aktualisieren der Statistiken:", err);
} finally {
setLoading(false);
}
};

return ( <div className="p-6 max-w-4xl mx-auto"> <h1 className="text-2xl font-bold mb-4 text-center">Statistiken</h1>

  <div className="mb-4 flex gap-2 items-center">
    <input
      type="number"
      placeholder="Student ID"
      value={studentFilter}
      onChange={(e) =>
        setStudentFilter(e.target.value ? parseInt(e.target.value) : "")
      }
      className="border p-2 rounded"
    />
    <input
      type="text"
      placeholder="Fach"
      value={subjectFilter}
      onChange={(e) => setSubjectFilter(e.target.value)}
      className="border p-2 rounded"
    />
    <button
      onClick={fetchStats}
      className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
    >
      Filtern
    </button>
    <button
      onClick={handleRefresh}
      className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
    >
      Statistiken aktualisieren
    </button>
  </div>

  {loading ? (
    <p>Lade Statistiken...</p>
  ) : stats.length === 0 ? (
    <p>Keine Statistiken vorhanden.</p>
  ) : (
    <table className="w-full border-collapse border border-gray-300">
      <thead className="bg-gray-200">
        <tr>
          <th className="border p-6">ID</th>
          <th className="border p-6">Schüler-ID</th>
          <th className="border p-6">Fach</th>
          <th className="border p-6">Höchste Note</th>
          <th className="border p-6">Niedrigste Note</th>
          <th className="border p-6">Durchschnitt</th>
        </tr>
      </thead>
      <tbody>
        {stats.map((s) => (
          <tr key={s.id} className="hover:bg-gray-50">
            <td className="border p-6">{s.id}</td>
            <td className="border p-6">{s.studentId}</td>
            <td className="border p-6">{s.subject}</td>
            <td className="border p-6">{s.highestGrade}</td>
            <td className="border p-6">{s.lowestGrade}</td>
            <td className="border p-6">{s.averageGrade.toFixed(2)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  )}
</div>
);
};

export default StatsPage;