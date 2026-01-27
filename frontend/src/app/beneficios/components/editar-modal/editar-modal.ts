import { Component, EventEmitter, inject, Output, signal, TemplateRef, ViewChild, WritableSignal } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { BeneficioDTO } from '../../../api';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-editar-modal',
  imports: [FormsModule],
  templateUrl: './editar-modal.html',
  styleUrl: './editar-modal.css',
})
export class EditarModal {

  private modalService = inject(NgbModal);
	closeResult: WritableSignal<string> = signal('');
  @ViewChild('content') contentTemplate!: TemplateRef<any>;

  @Output() editado = new EventEmitter<BeneficioDTO>();

  beneficioEditar : BeneficioDTO = { nome: '', descricao: '', valor: 0.00, ativo: true };

	open(beneficio: BeneficioDTO) {
    this.beneficioEditar = { ...beneficio };
		this.modalService.open(this.contentTemplate, { ariaLabelledBy: 'modal-basic-title' });
	}

  editar() {
    this.editado.emit(this.beneficioEditar);
  }

}
