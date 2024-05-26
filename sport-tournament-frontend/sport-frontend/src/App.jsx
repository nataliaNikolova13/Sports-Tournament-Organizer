import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import RegistrationForm from "./forms/registration/Registration";
import LoginForm from "./forms/login/Login";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginForm></LoginForm>}></Route>
        <Route
          path="/registration"
          element={<RegistrationForm></RegistrationForm>}
        ></Route>
      </Routes>
    </Router>
  );
}

export default App;
