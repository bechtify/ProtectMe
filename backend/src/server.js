const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");

//const authRoutes = require("./routes/authRoutes");
const userRoutes = require("./routes/userRoutes");
//const litRoutes = require("./routes/litRoutes");
const env = require("./config/env");

const server = express();

// Enable CORS
server.use(cors());

// Using bodyParser to parse JSON bodies into JS objects ==> https://github.com/expressjs/body-parser
server.use(bodyParser.urlencoded({ extended: false }));
server.use(bodyParser.json());

// Set all routes to /router/{specific}Routes.js
//server.use("/auth", authRoutes);
server.use("/users", userRoutes);
//server.use("/lits", litRoutes);

server.listen(env.port, () => {
  console.log("Server started on Port "+env.port);
});