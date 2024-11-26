const sqlite3 = require('sqlite3').verbose()
const { open } = require('sqlite')

const request = async function () {
  const db = await open({
    filename: './database.db',
    driver: sqlite3.Database
  })
  await db.exec('CREATE TABLE managers (id INTEGER, name TEXT, PRIMARY KEY(id))')
  await db.exec(
    'CREATE TABLE types (' +
      'id INTEGER,' +
      'name TEXT,' +
      'harwares_id Integer,' +
      'FOREIGN KEY (hardwares_id) REFERENCES hardwares(id),' +
      'PRIMARY KEY(id))'
  )
  await db.exec('CREATE TABLE suppliers (id INTEGER, name TEXT, PRIMARY KEY(id))')
  await db.exec(
    'CREATE TABLE equipments (' +
      'id INTEGER,' +
      'name TEXT NOT NULL,' +
      'supplier_id INTEGER,' +
      'type_id INTEGER,' +
      'FOREIGN KEY (supplier_id) REFERENCES suppliers(id),' +
      'FOREIGN KEY (type_id) REFERENCES types(id),' +
      'PRIMARY KEY(id)' +
      ')'
  )
  await db.exec('CREATE TABLE users (id INTEGER, name TEXT, PRIMARY KEY(id))')
  await db.exec(
    'CREATE TABLE positions (' +
      'id INTEGER,' +
      'name TEXT,' +
      'user_id INTEGER,' +
      'FOREIGN KEY (user_id) REFERENCES users(id),' +
      'PRIMARY KEY(id)' +
      ')'
  )
  await db.exec(
    'CREATE TABLE locations (' +
      'id INTEGER,' +
      'name TEXT,' +
      'town TEXT,' +
      'office TEXT,' +
      'department TEXT,' +
      'PRIMARY KEY(id)' +
      ')'
  )
  await db.exec(
    'CREATE TABLE faults (' +
      'id INTEGER,' +
      'name TEXT,' +
      'harwares_id Integer,' +
      'FOREIGN KEY (hardwares_id) REFERENCES hardwares(id),' +
      'PRIMARY KEY(id))'
  )
  await db.exec('CREATE TABLE hardwares (id INTEGER, name TEXT, PRIMARY KEY(id))')
  db.close()
}

request()
  .then(() => {})
  .catch((err) => {
    console.log(err)
  })
