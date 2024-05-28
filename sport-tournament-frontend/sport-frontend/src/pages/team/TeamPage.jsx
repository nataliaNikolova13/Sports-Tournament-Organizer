import React, { useState, useEffect } from "react";
import axios from "axios";
import "./TeamPage.css";
import CreateTeam from "../../forms/teamCreate/CreateTeam";

const TeamPage = ({ decodeToken }) => {
  return (
    <>
      <CreateTeam></CreateTeam>
    </>
  );
};

export default TeamPage;
