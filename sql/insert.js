const sqlite3 = require('sqlite3').verbose()
const { open } = require('sqlite')

const request = async function () {
  const db = await open({
    filename: './database.db',
    driver: sqlite3.verbose().Database
  })
  for (let i = 0; i < 1000; i++) {
    await db.run(
      'INSERT INTO suppliers (name) VALUES (?)',
      'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'
    )
  }
  db.close()
}

request()
  .then(() => {})
  .catch((err) => {
    console.log(err)
  })
