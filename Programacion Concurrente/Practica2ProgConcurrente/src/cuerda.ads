-- Package cuerda
-- Se encarga de la gestion de acceso a la seccion critica.
package cuerda is
   type estado_cuerda is (Norte, Sur, Libre);
   
   protected type Monitor(Inicial: Natural) is
      entry entraCuerda(babuino: in  estado_cuerda; success: out Boolean; idx: in Integer);
      procedure saleCuerda(babuino: in  estado_cuerda; idx: in Integer);
   private
      Cap_Maxima: Natural := Inicial;
      En_Cuerda: Natural := 0;
      est: estado_cuerda := Libre;
   end Monitor;

end cuerda;
