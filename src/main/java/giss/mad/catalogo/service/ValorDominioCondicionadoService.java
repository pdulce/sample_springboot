package giss.mad.catalogo.service;

import giss.mad.catalogo.model.ValorDominioCondicionadoPor;
import giss.mad.catalogo.repository.ValorDominioCondicionadoRepository;
import giss.mad.catalogo.repository.ValorDominioRepository;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ValorDominioCondicionadoService {

  @Autowired
  private ValorDominioCondicionadoRepository valorDominioCondicionadoRepository;

  @Autowired
  private ValorDominioRepository valorDominioRepository;

  public final void setValorDominioCondicionadoRepository(
          final ValorDominioCondicionadoRepository valorDominioCondicionadoRepository) {
    this.valorDominioCondicionadoRepository = valorDominioCondicionadoRepository;
  }

  public final void setValorDominioRepository(final ValorDominioRepository valorDominioRepository) {
    this.valorDominioRepository = valorDominioRepository;
  }

  public final Collection<ValorDominioCondicionadoPor> getAll() {
    return this.valorDominioCondicionadoRepository.findAllByDeletedIsNull();
  }
  public final ValorDominioCondicionadoPor get(final Integer idvalorDominioConditionated) {
    return this.valorDominioCondicionadoRepository.findByIdAndDeletedIsNull(idvalorDominioConditionated);
  }


  @Transactional
  public final ValorDominioCondicionadoPor insertar(
      final ValorDominioCondicionadoPor valorDominioCondicionadoPor) {
    valorDominioCondicionadoPor.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
    return this.valorDominioCondicionadoRepository.save(valorDominioCondicionadoPor);
  }

  @Transactional
  public final ValorDominioCondicionadoPor update(final ValorDominioCondicionadoPor valorDominioConditionated) {
    ValorDominioCondicionadoPor bbddObject = this.valorDominioCondicionadoRepository.findByIdAndDeletedIsNull(
            valorDominioConditionated.getId());
    if (bbddObject != null) {
      valorDominioConditionated.setUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
      valorDominioConditionated.setCreationDate(bbddObject.getCreationDate());
      bbddObject = this.valorDominioCondicionadoRepository.save(valorDominioConditionated);
    }
    return bbddObject;
  }

  @Transactional
  public final ValorDominioCondicionadoPor remove(final Integer valorDominioConditionatedId) {
    ValorDominioCondicionadoPor bbddObject = this.valorDominioCondicionadoRepository.findByIdAndDeletedIsNull(
            valorDominioConditionatedId);
    if (bbddObject != null) {
      Timestamp timeStampNow = new Timestamp(Calendar.getInstance().getTime().getTime());
      bbddObject.setUpdateDate(timeStampNow);
      bbddObject.setDeleted(1);
      bbddObject = this.valorDominioCondicionadoRepository.save(bbddObject);
    }
    return bbddObject;
  }

   /*@Transactional
  public final ValorDominioCondicionadoPor removePhysical(final Integer idvalorDominioConditionated) {
    ValorDominioCondicionadoPor valorDominioConditionated = this.valorDominioCondicionadoRepository.
        findById(idvalorDominioConditionated).get();
    if (valorDominioConditionated != null) {
      this.valorDominioCondicionadoRepository.delete(valorDominioConditionated);
    }
    return valorDominioConditionated;
  }*/



}
