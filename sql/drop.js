const sqlite3 = require('sqlite3').verbose()
const { open } = require('sqlite')

const request = async function () {
  const db = await open({
    filename: './database.db',
    driver: sqlite3.verbose().Database
  })
  await db.exec('DROP TABLE suppliers')
  await db.exec('DROP TABLE types')
  await db.exec('DROP TABLE equipments')
  await db.exec('DROP TABLE positions')
  await db.exec('DROP TABLE users')
  await db.exec('DROP TABLE locations')
  db.close()
}

request()
  .then(() => {})
  .catch((err) => {
    console.log(err)
  })
