package funlearn.db

import com.augustnagro.magnum.*
import funlearn.model.Deck

class DbAccessLayerRepo[EC, E, Id](repo: Repo[EC, E, Id]):
  def findAll(spec: Spec[E]): Vector[E] =
    connect(transactor):
      repo.findAll(spec)

  def findAll(): Vector[E] =
    connect(transactor):
      repo.findAll

  def insertReturning(e: EC): E =
    connect(transactor):
      repo.insertReturning(e)

  def findById(id: Id): Option[E] =
    connect(transactor):
      repo.findById(id)

  def update(e: E): Unit =
    connect(transactor):
      repo.update(e)

  def deleteById(id: Id): Unit =
    connect(transactor):
      repo.deleteById(id)
end DbAccessLayerRepo

object DbAccessLayerRepo:
  def apply[EC, E, Id](repo: Repo[EC, E, Id]): DbAccessLayerRepo[EC, E, Id] =
    new DbAccessLayerRepo[EC, E, Id](repo)
