function initializeCoreMod() {
// BEGIN

Java.type("net.minecraftforge.coremod.api.ASMAPI").loadFile("easycorelib.js");

easycore.include("me");

var IngameGui = net.minecraft.client.gui.IngameGui;

// BIPUSH 24

// Draw full selected item graphic
easycore.inMethod(IngameGui.func_194806_b(float))
    .atFirst(bipush(22)).after(bipush(24)).replace(
        bipush(24)
    )

return easycore.build();

// END
}
