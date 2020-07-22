const express = require("express");
const https = require("https");
const bodyParser = require("body-parser");
const cors = require("cors");
const fs = require("fs")

const emergencyRoutes = require("./routes/emergencyRoutes");
const userRoutes = require("./routes/userRoutes");
const contactRoutes = require("./routes/contactRoutes");
const env = require("./config/env");

const server = express();

// Enable CORS
server.use(cors());

// Using bodyParser to parse JSON bodies into JS objects ==> https://github.com/expressjs/body-parser
server.use(bodyParser.urlencoded({ extended: false }));
server.use(bodyParser.json());

// Set all routes to /router/{specific}Routes.js
server.use("/emergencies", emergencyRoutes);
server.use("/users", userRoutes);
server.use("/contacts", contactRoutes);

// const privateKey = fs.readFileSync('/etc/letsencrypt/live/protectme.the-rothley.de/privkey.pem', 'utf8');
// const certificate = fs.readFileSync('/etc/letsencrypt/live/protectme.the-rothley.de/cert.pem', 'utf8');
// const ca = fs.readFileSync('/etc/letsencrypt/live/protectme.the-rothley.de/chain.pem', 'utf8');

// https.createServer({
//   key: privateKey,
//   cert: certificate,
//   ca: ca
// },server).listen(env.port, () => {
//   console.log("Server started on Port "+env.port);
// });

server.listen(env.port, () => {
  console.log("Server started on Port "+env.port);
});