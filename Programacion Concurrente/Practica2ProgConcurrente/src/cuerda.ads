package cuerda is
   type estado_cuerda is (Norte, Sur, Libre);
   
   protected type Monitor(Inicial: Natural) is
      entry entraCuerda(babuino: in  estado_cuerda; success: out Boolean; idx: in Integer);
      procedure saleCuerda(babuino: in  estado_cuerda; idx: in Integer);
   private
      Cap_Maxima: Natural := Inicial;
      En_Cuerda: Natural := 0;
      est: estado_cuerda := Libre;
      permiso: Boolean := true;
   end Monitor;

end cuerda;
