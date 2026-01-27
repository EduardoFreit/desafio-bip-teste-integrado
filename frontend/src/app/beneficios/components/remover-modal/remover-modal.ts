import { Component, EventEmitter, inject, Output, signal, TemplateRef, ViewChild, WritableSignal } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { BeneficioDTO } from '../../../api';

@Component({
  selector: 'app-remover-modal',
  imports: [],
  templateUrl: './remover-modal.html',
  styleUrl: './remover-modal.css',
})
export class RemoverModal {

  private modalService = inject(NgbModal);
	closeResult: WritableSignal<string> = signal('');
  @ViewChild('content') contentTemplate!: TemplateRef<any>;

  @Output() deletado = new EventEmitter<number>();

  beneficioDeletar : BeneficioDTO = { nome: '', descricao: '', valor: 0.00, ativo: true };
	
  open(beneficio: BeneficioDTO) {
    this.beneficioDeletar = beneficio;
		this.modalService.open(this.contentTemplate, { ariaLabelledBy: 'modal-basic-title' });
	}

  remover() {
    this.deletado.emit(this.beneficioDeletar.id!);
  }

}
