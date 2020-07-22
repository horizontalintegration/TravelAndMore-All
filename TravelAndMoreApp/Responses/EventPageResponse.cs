using AppResponseWebApi.Helper;
using Newtonsoft.Json;

namespace AppResponseWebApi.Responses
{
    [JsonConverter(typeof(JsonPathConverter))]
    public class EventPageResponse
    {
        [JsonProperty("fields.Title.value")]
        public string Title { get; set; }
        [JsonProperty("fields.Summary.value")]
        public string Summary { get; set; }

        [JsonProperty("placeholders.main[?(@.componentName == 'Event')]")]
        public ComponentDetail EventDetail { get; set; }
    }
}
