import React from "react";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import Users from "./pages/Users";
import Classes from "./pages/Classes";
import Grades from "./pages/Grades";
import Stats from "./pages/Stats";

export default function App() {
  return (
    <BrowserRouter>
      <nav style={{display:"flex", gap:"1rem", padding:"1rem", background:"#eee"}}>
        <Link to="/users">Benutzer</Link>
        <Link to="/classes">Klassen</Link>
        <Link to="/grades">Noten</Link>
        <Link to="/stats">Statistik</Link>
      </nav>
      <Routes>
        <Route path="/users" element={<Users/>}/>
        <Route path="/classes" element={<Classes/>}/>
        <Route path="/grades" element={<Grades/>}/>
        <Route path="/stats" element={<Stats/>}/>
      </Routes>
    </BrowserRouter>
  );
}
