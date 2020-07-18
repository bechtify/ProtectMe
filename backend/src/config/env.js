/////////////////////////////////////////////////////////////////////////
//  config.js  //////////////////////////////////////////////////////////
// Will be served with information from the .env file via the 'dotenv' //
// NPM-Packet. Export all environment variables from here!             //
/////////////////////////////////////////////////////////////////////////
require("dotenv").config();
module.exports = {
  port: process.env.PORT,
  dbConfig: {
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PW,
    database: process.env.DB_NAME
  }
};
