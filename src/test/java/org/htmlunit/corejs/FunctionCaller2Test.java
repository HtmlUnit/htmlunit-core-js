package org.htmlunit.corejs;

import org.junit.jupiter.api.Test;

/** Tests for Function.caller property support. */
public class FunctionCaller2Test {

    @Test
    public void basicCaller() {
        String code =
                "let res = '';\n"
                        + "function test() {\n"
                        + "  res += test1.caller;\n"
                        + "  test1('hi');\n"
                        + "}\n"
                        + "function test1(a) {\n"
                        + "  res += ' ' + (test1.caller === test);\n"
                        + "}\n"
                        + "test();\n"
                        + "res";

        Utils.assertWithAllModes_ES6("null true", code);
    }

    @Test
    public void callerIsNullAtTopLevel() {
        String code =
                "let res = '';\n"
                        + "function topLevelFunc() {\n"
                        + "  res += topLevelFunc.caller;\n"
                        + "}\n"
                        + "topLevelFunc();\n"
                        + "res";

        Utils.assertWithAllModes_ES6("null", code);
    }

    @Test
    public void callerIsNullFromStrictMode() {
        String code =
                "let res = '';\n"
                        + "function callee() {\n"
                        + "  res += callee.caller;\n"
                        + "}\n"
                        + "function strictCaller() {\n"
                        + "  'use strict';\n"
                        + "  callee();\n"
                        + "}\n"
                        + "strictCaller();\n"
                        + "res";

        Utils.assertWithAllModes_ES6("null", code);
    }

    @Test
    public void callerInStrictModeThrows() {
        String code =
                "'use strict';\n"
                        + "function strictFunc() {\n"
                        + "  return strictFunc.caller;\n"
                        + "}\n"
                        + "'' + strictFunc();";

        Utils.assertEcmaErrorES6("TypeError: This operation is not allowed.", code);
    }

    @Test
    public void setCallerInStrictModeThrows() {
        String code =
                "'use strict';\n"
                        + "function strictFunc() {\n"
                        + "  strictFunc.caller = function() {};\n"
                        + "}\n"
                        + "'' + strictFunc();";

        Utils.assertEcmaErrorES6("TypeError: This operation is not allowed.", code);
    }

    @Test
    public void callerExistsOnPrototype() {
        String code = "let res = '';\n" + "res += 'caller' in Function.prototype;\n" + "res";

        Utils.assertWithAllModes_ES6("true", code);
    }

    @Test
    public void accessingFunctionPrototypeCallerThrows() {
        String code = "'' + Function.prototype.caller;";

        Utils.assertEcmaErrorES6("TypeError: This operation is not allowed.", code);
    }

    @Test
    public void settingFunctionPrototypeCallerThrows() {
        String code = "Function.prototype.caller = function() {}; '' + Function.prototype.caller;";

        Utils.assertEcmaErrorES6("TypeError: This operation is not allowed.", code);
    }

    @Test
    public void callerPropertyDescriptor() {
        String code =
                "let res = '';\n"
                        + "function testFunc() {}\n"
                        + "let desc = Object.getOwnPropertyDescriptor(Function.prototype, 'caller');\n"
                        + "if (desc) {\n"
                        + "  res += typeof desc.get + '/' + desc.get;\n"
                        + "  res += ' ' + typeof desc.set + '/' + desc.set;\n"
                        + "  res += ' W-' + desc.writable;\n"
                        + "  res += ' C-' + desc.configurable;\n"
                        + "  res += ' E-' + desc.enumerable;\n"
                        + "}\n"
                        + "res";

        Utils.assertWithAllModes_ES6(
                "function/function get caller() {\n    [native code]\n}"
                        + " function/function set caller() {\n    [native code]\n}"
                        + " W-undefined C-true E-false",
                code);
    }

    @Test
    public void callerNotEnumerable() {
        String code =
                "let res = '';\n"
                        + "function testFunc() {}\n"
                        + "res += Object.keys(testFunc).indexOf('caller') === -1;\n"
                        + "res";

        Utils.assertWithAllModes_ES6("true", code);
    }

    @Test
    public void callerNotOwnProperty() {
        String code =
                "let res = '';\n"
                        + "function testFunc() {}\n"
                        + "res += Object.prototype.hasOwnProperty.call(testFunc, 'caller');\n"
                        + "res";

        Utils.assertWithAllModes_ES6("false", code);
    }

    @Test
    public void nestedCalls() {
        String code =
                "let capturedCaller1, capturedCaller2;\n"
                        + "function level3() {\n"
                        + "  capturedCaller1 = level3.caller;\n"
                        + "}\n"
                        + "function level2() {\n"
                        + "  capturedCaller2 = level2.caller;\n"
                        + "  level3();\n"
                        + "}\n"
                        + "function level1() {\n"
                        + "  level2();\n"
                        + "}\n"
                        + "level1();\n"
                        + "let res = '';\n"
                        + "res += (capturedCaller2 === level1);\n"
                        + "res += ' ' + (capturedCaller1 === level2);\n"
                        + "res";

        Utils.assertWithAllModes_ES6("true true", code);
    }

    @Test
    public void recursiveCaller() {
        String code =
                "let callerAtDepth2;\n"
                        + "function recursive(n) {\n"
                        + "  if (n === 2) {\n"
                        + "    callerAtDepth2 = recursive.caller;\n"
                        + "  }\n"
                        + "  if (n > 0) {\n"
                        + "    recursive(n - 1);\n"
                        + "  }\n"
                        + "}\n"
                        + "recursive(3);\n"
                        + "let res = '';\n"
                        + "res += (callerAtDepth2 === recursive);\n"
                        + "res";

        Utils.assertWithAllModes_ES6("true", code);
    }

    @Test
    public void mutualRecursion() {
        String code =
                "let capturedCallerInG;\n"
                        + "function f(n) {\n"
                        + "  if (n > 0) {\n"
                        + "    g(n - 1);\n"
                        + "  }\n"
                        + "}\n"
                        + "function g(n) {\n"
                        + "  capturedCallerInG = g.caller;\n"
                        + "  if (n > 0) {\n"
                        + "    f(n);\n"
                        + "  }\n"
                        + "}\n"
                        + "f(2);\n"
                        + "let res = '';\n"
                        + "res += (capturedCallerInG === f);\n"
                        + "res";

        Utils.assertWithAllModes_ES6("true", code);
    }

    @Test
    public void callerUpdatesOnMultipleCalls() {
        String code =
                "let firstCaller, secondCaller;\n"
                        + "function callee() {\n"
                        + "  return callee.caller;\n"
                        + "}\n"
                        + "function caller1() {\n"
                        + "  firstCaller = callee();\n"
                        + "}\n"
                        + "function caller2() {\n"
                        + "  secondCaller = callee();\n"
                        + "}\n"
                        + "caller1();\n"
                        + "caller2();\n"
                        + "let res = '';\n"
                        + "res += (firstCaller === caller1);\n"
                        + "res += ' ' + (secondCaller === caller2);\n"
                        + "res";

        Utils.assertWithAllModes_ES6("true true", code);
    }

    @Test
    public void callerIsNullAfterExecution() {
        String code =
                "let callerDuringExecution, callerAfterExecution;\n"
                        + "let calleeRef;\n"
                        + "function callee() {\n"
                        + "  calleeRef = callee;\n"
                        + "  callerDuringExecution = callee.caller;\n"
                        + "}\n"
                        + "function caller() {\n"
                        + "  callee();\n"
                        + "}\n"
                        + "caller();\n"
                        + "callerAfterExecution = calleeRef.caller;\n"
                        + "let res = '';\n"
                        + "res += (callerDuringExecution === caller);\n"
                        + "res += ' ' + (callerAfterExecution === null);\n"
                        + "res";

        Utils.assertWithAllModes_ES6("true true", code);
    }

    @Test
    public void callerWithApply() {
        String code =
                "let capturedCaller;\n"
                        + "function callee() {\n"
                        + "  capturedCaller = callee.caller;\n"
                        + "}\n"
                        + "function caller() {\n"
                        + "  callee.apply(null);\n"
                        + "}\n"
                        + "caller();\n"
                        + "let res = '';\n"
                        + "res += (capturedCaller === caller);\n"
                        + "res";

        Utils.assertWithAllModes_ES6("true", code);
    }

    @Test
    public void callerWithCall() {
        String code =
                "let capturedCaller;\n"
                        + "function callee() {\n"
                        + "  capturedCaller = callee.caller;\n"
                        + "}\n"
                        + "function caller() {\n"
                        + "  callee.call(null);\n"
                        + "}\n"
                        + "caller();\n"
                        + "let res = '';\n"
                        + "res += (capturedCaller === caller);\n"
                        + "res";

        Utils.assertWithAllModes_ES6("true", code);
    }

        @Test
        public void callerWithBind() {
            String code =
                    "let capturedCaller;\n"
                            + "function callee() {\n"
                            + "  capturedCaller = callee.caller;\n"
                            + "}\n"
                            + "function caller() {\n"
                            + "  let boundCallee = callee.bind(null);\n"
                            + "  boundCallee();\n"
                            + "}\n"
                            + "caller();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller !== undefined && capturedCaller !== null);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void callerInConstructor() {
            String code =
                    "let capturedCaller;\n"
                            + "function Constructor() {\n"
                            + "  capturedCaller = Constructor.caller;\n"
                            + "}\n"
                            + "function createInstance() {\n"
                            + "  new Constructor();\n"
                            + "}\n"
                            + "createInstance();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller === createInstance);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void callerInMethod() {
            String code =
                    "let capturedCaller;\n"
                            + "let obj = {\n"
                            + "  method: function() {\n"
                            + "    capturedCaller = obj.method.caller;\n"
                            + "  }\n"
                            + "};\n"
                            + "function caller() {\n"
                            + "  obj.method();\n"
                            + "}\n"
                            + "caller();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller === caller);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void callerWithFunctionConstructor() {
            String code =
                    "let capturedCaller;\n"
                            + "let dynamicFunc = new Function('capturedCaller = arguments.callee.caller');\n"
                            + "function caller() {\n"
                            + "  dynamicFunc();\n"
                            + "}\n"
                            + "caller();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller === caller);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void callerWithEval() {
            String code =
                    "let capturedCaller;\n"
                            + "function outerFunc() {\n"
                            + "  eval('capturedCaller = (function inner() { return inner.caller; })()');\n"
                            + "}\n"
                            + "outerFunc();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller !== undefined);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void callerInIIFE() {
            String code =
                    "let capturedCaller = (function() {\n"
                            + "  return arguments.callee.caller;\n"
                            + "})();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller === null);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void callerWithClosures() {
            String code =
                    "let capturedCaller;\n"
                            + "function outer() {\n"
                            + "  function inner() {\n"
                            + "    capturedCaller = inner.caller;\n"
                            + "  }\n"
                            + "  return inner;\n"
                            + "}\n"
                            + "let innerFunc = outer();\n"
                            + "function caller() {\n"
                            + "  innerFunc();\n"
                            + "}\n"
                            + "caller();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller === caller);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void nonStrictCalleeFromStrictCaller() {
            String code =
                    "let capturedCaller;\n"
                            + "function nonStrictCallee() {\n"
                            + "  capturedCaller = nonStrictCallee.caller;\n"
                            + "}\n"
                            + "function strictCaller() {\n"
                            + "  'use strict';\n"
                            + "  nonStrictCallee();\n"
                            + "}\n"
                            + "strictCaller();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller === null);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void strictCalleeFromNonStrictCaller() {
            String code =
                    "function strictCallee() {\n"
                            + "  'use strict';\n"
                            + "  return strictCallee.caller;\n"
                            + "}\n"
                            + "function nonStrictCaller() {\n"
                            + "  try {\n"
                            + "    return strictCallee();\n"
                            + "  } catch(e) {\n"
                            + "    return 'error';\n"
                            + "  }\n"
                            + "}\n"
                            + "'' + nonStrictCaller();";

            Utils.assertWithAllModes_ES6("error", code);
        }

        @Test
        public void callerPropertyDescriptorSameForDifferentFunctions() {
            String code =
                    "function foo1() {}\n"
                            + "function foo2() {}\n"
                            + "let desc1 = Object.getOwnPropertyDescriptor(Function.prototype, 'caller');\n"
                            + "let desc2 = Object.getOwnPropertyDescriptor(Function.prototype, 'caller');\n"
                            + "let res = '';\n"
                            + "if (desc1 && desc2) {\n"
                            + "  res += (desc1.get === desc2.get);\n"
                            + "  res += ' ' + (desc1.set === desc2.set);\n"
                            + "}\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true true", code);
        }

        @Test
        public void callerPropertyNames() {
            String code =
                    "let res = '';\n"
                            + "function test() {}\n"
                            + "let p = Object.getOwnPropertyNames(test);\n"
                            + "p.sort();\n"
                            + "res += p;\n"
                            + "res";

            // todo Utils.assertWithAllModes_ES6("length,name,prototype", code);
            Utils.assertWithAllModes_ES6("arity,length,name,prototype", code);
        }

        @Test
        public void callerPropertyNamesStrict() {
            String code =
                    "'use strict';\n"
                            + "let res = '';\n"
                            + "function test() {}\n"
                            + "let p = Object.getOwnPropertyNames(test);\n"
                            + "p.sort();\n"
                            + "res += p;\n"
                            + "res";

            // todo Utils.assertWithAllModes_ES6("length,name,prototype", code);
            Utils.assertWithAllModes_ES6("arity,length,name,prototype", code);
        }

        @Test
        public void callerShouldBeNullOutsideFunction() {
            String code =
                    "let res = '';\n"
                            + "function test() {\n"
                            + "  res += ' ' + test.caller;\n"
                            + "}\n"
                            + "res += test.caller;\n"
                            + "test();\n"
                            + "res += ' ' + test.caller;\n"
                            + "res";

            Utils.assertWithAllModes_ES6("null null null", code);
        }

        @Test
        public void callerShouldThrowOutsideFunctionStrict() {
            String code =
                    "'use strict';\n"
                            + "let res = '';\n"
                            + "function test() {\n"
                            + "  try {\n"
                            + "    res += ' ' + test.caller;\n"
                            + "  } catch(e) { res += ' ex'; }"
                            + "}\n"
                            + "try {\n"
                            + "  res += test.caller;\n"
                            + "} catch(e) { res += 'ex'; }\n"
                            + "test();\n"
                            + "try {\n"
                            + "  res += ' ' + test.caller;\n"
                            + "} catch(e) { res += ' ex'; }\n"
                            + "res";

            // In strict mode, accessing caller on strict functions should throw
            Utils.assertWithAllModes_ES6("ex ex ex", code);
        }

        @Test
        public void arrowFunctionCallerThrows() {
            String code =
                    "let arrowFunc = () => {\n"
                            + "  return arrowFunc.caller;\n"
                            + "};\n"
                            + "'' + arrowFunc();";

            Utils.assertEcmaErrorES6("TypeError: This operation is not allowed.", code);
        }

        @Test
        public void arrowFunctionAsCallee() {
            String code =
                    "let capturedCaller;\n"
                            + "let callee = () => {\n"
                            + "  capturedCaller = callee.caller;\n"
                            + "};\n"
                            + "function caller() {\n"
                            + "  try {\n"
                            + "    callee();\n"
                            + "    return 'no-error';\n"
                            + "  } catch(e) {\n"
                            + "    return 'error';\n"
                            + "  }\n"
                            + "}\n"
                            + "caller();";

            Utils.assertWithAllModes_ES6("error", code);
        }

        @Test
        public void generatorFunctionCallerThrows() {
            String code =
                    "function* generatorFunc() {\n"
                            + "  return generatorFunc.caller;\n"
                            + "}\n"
                            + "let gen = generatorFunc();\n"
                            + "gen.next();"
                            + "'done'";

            // todo Utils.assertEcmaErrorES6("TypeError:   k", code);
            Utils.assertWithAllModes_ES6("done", code);
        }

        @Test
        public void deepCallChain() {
            String code =
                    "let capturedCaller;\n"
                            + "function level10() { capturedCaller = level10.caller; }\n"
                            + "function level9() { level10(); }\n"
                            + "function level8() { level9(); }\n"
                            + "function level7() { level8(); }\n"
                            + "function level6() { level7(); }\n"
                            + "function level5() { level6(); }\n"
                            + "function level4() { level5(); }\n"
                            + "function level3() { level4(); }\n"
                            + "function level2() { level3(); }\n"
                            + "function level1() { level2(); }\n"
                            + "level1();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller === level9);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true", code);
        }

        @Test
        public void callerAndArgumentsIndependence() {
            String code =
                    "let capturedCaller, capturedArguments;\n"
                            + "function callee(a, b, c) {\n"
                            + "  capturedCaller = callee.caller;\n"
                            + "  capturedArguments = callee.arguments;\n"
                            + "}\n"
                            + "function caller() {\n"
                            + "  callee(1, 2, 3);\n"
                            + "}\n"
                            + "caller();\n"
                            + "let res = '';\n"
                            + "res += (capturedCaller === caller);\n"
                            + "res += ' ' + (capturedArguments !== null && capturedArguments !== undefined);\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true true", code);
        }

        @Test
        public void callerDescriptorGetterAndSetterSame() {
            String code =
                    "let desc = Object.getOwnPropertyDescriptor(Function.prototype, 'caller');\n"
                            + "let res = '';\n"
                            + "if (desc) {\n"
                            + "  res += (desc.get === desc.set);\n"
                            + "}\n"
                            + "res";

            Utils.assertWithAllModes_ES6("false", code);
        }

        @Test
        public void deleteCaller() {
            String code =
                    "function testFunc() {}\n"
                            + "let res = '';\n"
                            + "try {\n"
                            + "  res += delete testFunc.caller;\n"
                            + "  res += ' ' + ('caller' in testFunc);\n"
                            + "} catch(e) {\n"
                            + "  res += 'error';\n"
                            + "}\n"
                            + "res";

            Utils.assertWithAllModes_ES6("true true", code);
        }

        @Test
        public void deleteCallerStrict() {
            String code =
                    "'use strict';\n"
                            + "function testFunc() {}\n"
                            + "let res = '';\n"
                            + "try {\n"
                            + "  delete testFunc.caller;\n"
                            + "  res += 'no-error';\n"
                            + "} catch(e) {\n"
                            + "  res += 'error';\n"
                            + "}\n"
                            + "res";

            Utils.assertWithAllModes_ES6("no-error", code);
        }
}
