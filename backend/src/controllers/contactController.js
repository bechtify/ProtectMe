const userService = require("../services/contactService");
const contactService = require("../services/contactService");

// Handle request for GET /contacts/user/:userId
const getContacts = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    let result = await contactService.selectContactsByUserId(userId);
    if (result) {
      res.send(result);
    } else {
      res.send("Contacts not found!");
    }
  } catch (e) {
    res.send({ error: e });
  }
};

// Handle request for GET /contacts/:contactId
const getContact = async (req, res) => {
  try {
    const contactId = parseInt(req.params.contactId);
    let result = await contactService.selectContactById(contactId);
    if (result) {
      res.send(result);
    } else {
      res.send("Contact not found!");
    }
  } catch (e) {
    res.send({ error: e });
  }
};

// Handle request for POST /contacts/:userId
const addContact = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    const contactInfo = req.body;
    await contactService.addContact(contactInfo, userId);
    res.send();
  } catch (e) {
    res.send({ error: e });
  }
};

// Handle request for PUT /contacts/:contactId
const editContact = async (req, res) => {
  try {
    const contactId = parseInt(req.params.contactId);
    const contactInfo = req.body;
    await contactService.editContact(contactInfo, contactId);
    res.send();
  } catch (e) {
    res.send({ error: e });
  }
};

// Handle request for DELETE /contacts/:userId
const deleteContacts = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    const contactIds = req.body;
    await userService.postEmergency(contactIds, userId);
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
  deleteContacts,
};
