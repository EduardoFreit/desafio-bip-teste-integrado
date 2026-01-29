import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Beneficios } from './beneficios';
import { GerenciamentoDeBenefciosService, PageBeneficioDTO } from '../api';
import { provideToastr } from 'ngx-toastr';
import { of } from 'rxjs';

describe('Beneficios', () => {
  let component: Beneficios;
  let fixture: ComponentFixture<Beneficios>;
  const mockBeneficios: PageBeneficioDTO = {
    content: [
      { id: 1, nome: 'Vale Refeição', descricao: 'Auxílio alimentação', valor: 500, ativo: true },
      { id: 2, nome: 'Auxílio Creche', descricao: 'Para filhos até 5 anos', valor: 300, ativo: false }
    ]
  };

  beforeEach(async () => {

    const beneficioServiceMock = {
      listar: vi.fn().mockReturnValue(of({ content: [], totalElements: 0 }))
    };

    TestBed.configureTestingModule({
      imports: [Beneficios],
      providers: [
        provideToastr(),
        { provide: GerenciamentoDeBenefciosService, useValue: beneficioServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Beneficios);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Beneficio - Listagem', () => {
    it('deve renderizar a quantidade correta de linhas', async () => {
      component.beneficios.set([...mockBeneficios.content!]);
      component.collectionSize.set(mockBeneficios.content!.length);

      await fixture.whenStable();

      const rows = fixture.nativeElement.querySelectorAll('tbody tr');
      expect(rows.length).toBe(mockBeneficios.content?.length);
    });

    it('deve exibir mensagem de lista vazia quando não houver benefícios', async () => {
      component.beneficios.set([]);
      component.collectionSize.set(0);
      fixture.detectChanges();

      await fixture.whenStable();

      const tableBody = fixture.nativeElement.querySelector('tbody tr td');
      expect(tableBody.textContent).toContain('Nenhum Benefício Encontrado');
    });

    it('deve buscar benefícios filtrados do servidor', async () => {
      const beneficioService = TestBed.inject(GerenciamentoDeBenefciosService);

      const mockFiltrado = {
        content: [mockBeneficios.content![0]], // Apenas 'Vale Refeição'
        totalElements: 1
      };

      const spy = vi.spyOn(beneficioService, 'listar').mockReturnValue(of(mockFiltrado) as any);

      component.filtroNome.set('Vale Refeição');

      component.recarregarBeneficios();

      fixture.detectChanges();
      await fixture.whenStable();

      const rows = fixture.nativeElement.querySelectorAll('tbody tr');

      expect(spy).toHaveBeenCalled();
      expect(rows.length).toBe(1);
      expect(rows[0].textContent).toContain('Vale Refeição');
    });
  });

});
