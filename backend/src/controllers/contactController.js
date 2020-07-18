const userService = require("../services/contactService");

// Handle request for GET /contacts/user/:userId
const getContacts = async (req, res) => {
    try {
        const userId = parseInt(req.params.userId);
      await userService.postEmergency(req.body, userId);
      res.send();
    } catch (e) {
      res.send({ error: e });
    }
  };

// Handle request for GET /contacts/:contactId
const getContact = async (req, res) => {
    try {
        const userId = parseInt(req.params.userId);
      await userService.postEmergency(req.body, userId);
      res.send();
    } catch (e) {
      res.send({ error: e });
    }
  };

// Handle request for PUT /contacts/:contactId
const editContact = async (req, res) => {
    try {
        const userId = parseInt(req.params.userId);
      await userService.postEmergency(req.body, userId);
      res.send();
    } catch (e) {
      res.send({ error: e });
    }
  };

// Handle request for POST /contacts/:userId
const addContact = async (req, res) => {
    try {
        const userId = parseInt(req.params.userId);
      await userService.postEmergency(req.body, userId);
      res.send();
    } catch (e) {
      res.send({ error: e });
    }
  };

// Handle request for DELETE /contacts/:userId
const deleteContacts = async (req, res) => {
    try {
        const userId = parseInt(req.params.userId);
      await userService.postEmergency(req.body, userId);
      res.send();
    } catch (e) {
      res.send({ error: e });
    }
  };

  module.exports = {
    getContacts,
    getContact,
    editContact,
    addContact,
    deleteContacts
  };