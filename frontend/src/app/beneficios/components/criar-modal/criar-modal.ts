import { Component, EventEmitter, inject, Output, signal, TemplateRef, ViewChild, WritableSignal } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { BeneficioDTO } from '../../../api';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-criar-modal',
  imports: [FormsModule],
  templateUrl: './criar-modal.html',
  styleUrl: './criar-modal.css',
})
export class CriarModal {

  private modalService = inject(NgbModal);
	closeResult: WritableSignal<string> = signal('');
  @ViewChild('content') contentTemplate!: TemplateRef<any>;

  @Output() cadastrado = new EventEmitter<BeneficioDTO>();

  beneficioCadastro = signal<BeneficioDTO>(this.setarBeneficioCriacao());

  private setarBeneficioCriacao(): BeneficioDTO {
    return { nome: '', descricao: '', valor: 0.00, ativo: true };
  }

	open() {
    this.beneficioCadastro.set(this.setarBeneficioCriacao());
		this.modalService.open(this.contentTemplate, { ariaLabelledBy: 'modal-basic-title' });
	}

  cadastrar() {
    this.cadastrado.emit(this.beneficioCadastro());
  }

}
