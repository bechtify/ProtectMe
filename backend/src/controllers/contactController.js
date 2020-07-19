const contactService = require("../services/contactService");

// Handle request for GET /contacts/user/:userId
const getContacts = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    let result = await contactService.selectContactsByUserId(userId);
    if (result) {
      res.status(200).json(result);
    } else {
      res.status(404).json("Contacts not found!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};

// Handle request for GET /contacts/:contactId
const getContact = async (req, res) => {
  try {
    const contactId = parseInt(req.params.contactId);
    let result = await contactService.selectContactById(contactId);
    if (result) {
      res.status(200).json(result);
    } else {
      res.status(404).json("Contact not found!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};

// Handle request for POST /contacts/:userId
const addContact = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    const contactInfo = req.body;
    let result = await contactService.addContact(contactInfo, userId);
    if (result) {
      res.status(201).json(result);
    } else {
      res.status(400).json("Contact not added!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};

// Handle request for PUT /contacts/:contactId
const editContact = async (req, res) => {
  try {
    const contactId = parseInt(req.params.contactId);
    const contactInfo = req.body;
    let result = await contactService.editContact(contactInfo, contactId);
    if (!result) {
      res.status(200).json(result);
    } else {
      res.status(400).json("Contact not edited!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};

// Handle request for DELETE /contacts/:userId
const deleteContacts = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    const contactIds = req.body;
    let result = await contactService.deleteContacts(contactIds, userId);
    if (result) {
      res.sendStatus(200);
    } else {
      res.status(400).json("Contact not deleted!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};

module.exports = {
  getContacts,
  getContact,
  editContact,
  addContact,
  deleteContacts,
};
